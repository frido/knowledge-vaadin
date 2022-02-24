package com.example.application.services;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import com.example.application.knowledge.Car;
import com.example.application.knowledge.Item;
import com.example.application.knowledge.MessageQueue;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonDtoLombok;
import com.example.application.knowledge.PersonDtoRecord;
import com.example.application.knowledge.PersonWithVersion;
import com.example.application.knowledge.Person_;
import com.example.application.views.knowledge.LogType;
import org.hibernate.Session;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class EntityService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    TransactionalService transactionalService;

    @Autowired
    EntityNewService newService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public EntityManager getEm() {
        return em;
    }

    @Transactional
    public <T> T save(T entity) {
        return em.merge(entity);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    public <T> T find(Class<T> clazz) {
        return em.find(clazz, 1);
    }

    @Transactional
    public Person findAndEdit() {
        var person = em.find(Person.class, 1);
        person.setName("edited in service");
        return person;
    }

    @Transactional
    public PersonWithVersion findPersonWithVersion() {
        return em.find(PersonWithVersion.class, 1);
    }

    @Transactional
    public <T> T merge(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public Person findPersonTree() {
        Person person = em.find(Person.class, 1);
        person.getTeam(); 
        transactionalService.getTeamNameRequired(person);
        for (Item item : person.getItems()) {
            System.out.println(item);
        }
        return person;
    }

    @Transactional
    public PersonDtoRecord findPersonDto() {
        var cb = em.getCriteriaBuilder();
        CriteriaQuery<PersonDtoRecord> cq = cb.createQuery(PersonDtoRecord.class);
        Root<Person> root = cq.from(Person.class);
        cq.select(cb.construct(PersonDtoRecord.class, root.get(Person_.id), root.get(Person_.name),
                root.get(Person_.department), root.get(Person_.team)));
        cq.where(cb.equal(root.get(Person_.id), 1));
        TypedQuery<PersonDtoRecord> allQuery = em.createQuery(cq);
        return allQuery.getSingleResult();
    }

    @Transactional
    public PersonDtoLombok findPersonTouple() {
        var cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Person> root = query.from(Person.class);
        query.multiselect(root.get(Person_.id), root.get(Person_.name),
                root.get(Person_.department), root.get(Person_.team));
        query.where(cb.equal(root.get(Person_.id), 1));
        TypedQuery<Tuple> allQuery = em.createQuery(query);
        Tuple tuple = allQuery.getSingleResult();
        
        return PersonDtoLombok.builder()
        .id(tuple.get(root.get(Person_.id)))
        .name(tuple.get(root.get(Person_.name)))
        .department(tuple.get(root.get(Person_.department)))
        .team(tuple.get(root.get(Person_.team)))
        .build();
    }

    @Transactional(readOnly = true)
    public Person findPersonFetch() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> rootEntry = cq.from(Person.class);
        rootEntry.fetch(Person_.team, JoinType.LEFT);
        rootEntry.fetch(Person_.cars, JoinType.LEFT);
        rootEntry.fetch(Person_.items, JoinType.LEFT);
        cq.where(cb.equal(rootEntry.get(Person_.id), 1));
        CriteriaQuery<Person> all = cq.select(rootEntry);
        TypedQuery<Person> allQuery = em.createQuery(all);
        return allQuery.getSingleResult();
    }

    @Transactional
    public void run(Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public void onEditAllPersons() {
        List<Person> persons = findAll(Person.class);
        persons.forEach(p -> p.setName(randomText()));
    }

    private void log(String method, String payload) {
        MessageQueue.getInstance().add(LogType.APP, "View", method, payload);
    }

    @Transactional
    public void onAddCarToPerson() {
        Person person = find(Person.class);
        Car car = new Car();
        car.setSpz(randomText());
        person.getCars().add(car);
        person.setName(randomText());
        var className = "onAddCarToPerson";
        
        log(className, "person:" + person.getCars().stream().map(Car::toString).toList());
        log(className, "car:" + car);

        Person person2 = find(Person.class);
        log(className, "person2:" + person2.getCars().stream().map(Car::toString).toList());

        List<Car> cars = findAll(Car.class);
        log(className, "cars:" + cars.stream().map(Car::toString).toList());
    }

    @Transactional
    public void clearPersistentContext() {
        var person = find(PersonWithVersion.class);
        var cars = findAll(Car.class);
        var className = "clearPersistentContext";
        
        log(className, "before flush 1");
        em.flush();
        log(className, "after flush 1");
        
        log(className, "before clear");
        em.clear();
        log(className, "after clear");
        
        log(className, "before flush 2");
        em.flush();
        log(className, "after flush 2");

        log(className, "before find");
        var person2 = find(PersonWithVersion.class);
        log(className, "after find");

        log(className, EntityHelper.managetEntities(em));

        log(className, "before clear 2");
        em.clear();
        log(className, "after clear 2");

        log(className, EntityHelper.managetEntities(em));

        person2.setName(randomText());
        em.merge(person2);
        
    }

    @java.lang.SuppressWarnings({"squid:S1854", "java:S1481"})
    @Transactional(readOnly = true)
    public void reaOnlyTransaction() {
        var person = find(PersonWithVersion.class);
        var cars = findAll(Car.class);
        person.setName(randomText());
        var action = "reaOnlyTransaction";
        log(action, String.valueOf(em));
        SessionImplementor session = em.unwrap( SessionImplementor.class );
        String msg = "";
        for(Map.Entry<Object,EntityEntry> x : session.getPersistenceContext().reentrantSafeEntityEntries()) {
            msg = msg + ", " + String.valueOf(x.getKey());
        }
        log(action, msg);
        newService.reaOnlyTransaction2(person);
    }



    public void runBatch(Runnable runnable, int batchSize) {
        var hibernateSession = em.unwrap(Session.class);
        Integer oldBatchSize = hibernateSession.getJdbcBatchSize();
        try {
            hibernateSession.setJdbcBatchSize(batchSize);
            runnable.run();
            hibernateSession.flush();
        } finally {
            hibernateSession.setJdbcBatchSize(oldBatchSize);
        }
    }

    @Transactional
    public void onEditAllPersonsBatch() {
        runBatch(this::onEditAllPersons, 100);
    }

    private String randomText() {
        return LocalTime.now().toString();
    }

    // TODO: propagation, pridat propagacne logy aby user vecer co sa deje
    public void testing() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status1 = transactionManager.getTransaction(definition);
        var person1 = em.find(Person.class, 1);
        person1.setName("changed in 1 transaction");

        TransactionStatus status2 = transactionManager.getTransaction(definition);
        var person2 = em.find(Person.class, 1);
        person2.setName("changed in 2 transaction");

        transactionManager.commit(status2);
        var person3 = em.find(Person.class, 1);
        System.out.println(person3);

        transactionManager.commit(status1);
    }

    public void testing2() throws InterruptedException {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status1 = transactionManager.getTransaction(definition);
        var person1 = em.find(Person.class, 1);
        person1.setName("XXX");
        em.flush();
        Thread.sleep(30000);
        transactionManager.commit(status1);
    }

    public Person testing3() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status1 = transactionManager.getTransaction(definition);
        var person1 = em.find(Person.class, 1);
        transactionManager.commit(status1);
        return person1;
    }
}
