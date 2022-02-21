package com.example.application.services;

import java.time.LocalTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import com.example.application.knowledge.Item;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonDtoLombok;
import com.example.application.knowledge.PersonDtoRecord;
import com.example.application.knowledge.PersonWithVersion;
import com.example.application.knowledge.Person_;
import org.hibernate.Session;
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
    private PlatformTransactionManager transactionManager;

    public EntityManager getEm() {
        return em;
    }

    @Transactional // TODO: nerozumiem na co to tu je
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

    @Transactional // NOTE: not the same as find without transactional
    public Person findPersonTree() {
        Person person = em.find(Person.class, 1);
        person.getTeam(); // NOTE: toto nestaci
        transactionalService.getTeamNameRequired(person); // NOTE: musim zavolat toto aby sa to
                                                          // nacitalo
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
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // TODO:
                                                                                       // prepinatelne
                                                                                       // ako
                                                                                       // parameter
        // definition.setTimeout(3);
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

    private Person findAndEdit2() {
        var person = em.find(Person.class, 1);
        person.setName("edited in service");
        return person;
    }

    public void testing2() throws InterruptedException {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // TODO:
                                                                                           // prepinatelne
                                                                                           // ako
                                                                                           // parameter
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
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // TODO:
                                                                                       // prepinatelne
                                                                                       // ako
                                                                                       // parameter
        TransactionStatus status1 = transactionManager.getTransaction(definition);
        var person1 = em.find(Person.class, 1);
        transactionManager.commit(status1);
        return person1;
    }
}
