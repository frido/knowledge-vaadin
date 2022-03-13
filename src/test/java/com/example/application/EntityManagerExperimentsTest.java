package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import com.example.application.knowledge.Person;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@ComponentScan(value = "com.example.application")
public class EntityManagerExperimentsTest {

    private static Logger log = LoggerFactory.getLogger(EntityManagerExperimentsTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Entity manager has 1th level cache that allow repeatable reads. Even there is change in the
     * entity from another transaction, this is not reflected in find method. Entity from first
     * level cache is returned.
     * 
     * Workaround: call refresh on entity detach entity before find
     * 
     * Workaround doesn't work. It loads new instance of entity, but the name is still the same. It
     * seems cached on db (transaction) level.
     * 
     * Transaction that starts at the end see person correctly with edited name.
     * 
     * TX1 can not see changes in TX2 because it starts before TX2, but TX3 can see changes in TX2
     * because it starts after TX2.
     * 
     * One thing is Hibernate 1th level cache. But this is also about Transaction Isolation Level
     * What you can see in the middle of the transaction. Default was TRANSACTION_REPEATABLE_READ.
     * So transaction was repeatable read, even data was changed by TX2, TX1 can not see (even) commited changes.
     * 
     * Visibility of changes
     * TRANSACTION: propagation (each transaction has own entity manager), isolation (visibility of underlying changes)
     * ENTITY MANAGER: 1th level cache
     */
    @Test
    public void testTransactionIsolation() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();
        EntityManager em3 = entityManagerFactory.createEntityManager();

        Session session = em.unwrap(Session.class);
        session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ));
        session.doWork(connection -> log.info(String.valueOf(connection.getTransactionIsolation())));

        Map<String, String> variables = new HashMap<>();

        doInTransaction(em, () -> {
            Person personTx1Before = em.find(Person.class, 1);
            String personNameTx1Before = personTx1Before.getName();

            doInTransaction(em2, () -> {
                Person personTx2Before = em2.find(Person.class, 1);
                personTx2Before.setName(LocalDateTime.now().toString());
                variables.put("personNameTx2Before", personTx2Before.getName());
                em2.merge(personTx2Before);
                em2.flush();

                Person personTx2After = em2.find(Person.class, 1);
                printEquals(personTx2Before.getName(), personTx2After.getName());
            });

            em.clear(); // entity is reloaded from DB
            Person personTx1After = em.find(Person.class, 1);
            // The name should be the same as before transaction TX2 because of TRANSACTION_REPEATABLE_READ
            printEquals(personNameTx1Before, personTx1After.getName());
        });

        Person personEnd = em3.find(Person.class, 1);
        printEquals(variables.get("personNameTx2Before"), personEnd.getName());
    }

    /**
     * TODO: Concurent changes (locks)
     */
    @Test
    public void test1() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        Person person = em.find(Person.class, 1, LockModeType.PESSIMISTIC_WRITE);
        // Person person = em.find(Person.class, 1);
        person.setName(LocalDateTime.now().toString());
        // em.merge(person);
        // em.flush(); // flush sposobi LOCK riadku a commit v inej transakcii (em2) sa nevie
        // dokoncit

        em2.getTransaction().begin();
        Person person2 = em2.find(Person.class, 1);
        person2.setName(LocalDateTime.now().toString());
        em2.merge(person2);
        em2.flush(); // flush na LOCK-nutom riadku - waiting time exception
        em2.getTransaction().commit();

        em.getTransaction().commit();
    }

    private void printEquals(Object expected, Object actual) {
        log.info("expected: {}, actual: {}", expected, actual);
        assertEquals(expected, actual);
    }

    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

}
