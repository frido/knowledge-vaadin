package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * So transaction was repeatable read, even data was changed by TX2, TX1 can not see (even)
 * commited changes.
 * 
 * Visibility of changes TRANSACTION: propagation (each transaction has own entity manager),
 * isolation (visibility of underlying changes) ENTITY MANAGER: 1th level cache
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@ComponentScan(value = "com.example.application")
public class TransactionIsolationTest {

    private static final String PERSON_TX3 = "personNameEnd";
    private static final String PERSON_AFTER_TX = "personNameTx1After";
    private static final String PERSON_IN_TX = "personNameTx1Middle";
    private static final String PERSON_TX2 = "personNameTx2";
    private static final String PERSON_BEFORE_TX = "personNameTx1Before";

    private static Logger log = LoggerFactory.getLogger(ConcurrentChangesTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testTransactionIsolationReadUncommited() {
        testTransactionIsolationInternal(Arrays.asList(PERSON_IN_TX, PERSON_AFTER_TX, PERSON_TX3), Collections.emptyList(),
                Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    @Test
    public void testTransactionIsolationReadCommited() {
        testTransactionIsolationInternal(Arrays.asList(PERSON_AFTER_TX, PERSON_TX3), Arrays.asList(PERSON_IN_TX),
                Connection.TRANSACTION_READ_COMMITTED);
    }

    @Test
    public void testTransactionIsolationRepeatableRead() {
        testTransactionIsolationInternal(Arrays.asList(PERSON_TX3), Arrays.asList(PERSON_IN_TX, PERSON_AFTER_TX),
                Connection.TRANSACTION_REPEATABLE_READ);
    }

    // TODO: FAILED - Persimistick lock. Zistit viac.
    @Test
    public void testTransactionIsolationSerializable() {
        testTransactionIsolationInternal(Arrays.asList(PERSON_TX3), Arrays.asList(PERSON_IN_TX, PERSON_AFTER_TX),
                Connection.TRANSACTION_SERIALIZABLE);
    }

    public void testTransactionIsolationInternal(List<String> canSee, List<String> cannotSee,
            int connectionTransaction) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();
        EntityManager em3 = entityManagerFactory.createEntityManager();

        Session session = em.unwrap(Session.class);
        session.doWork(connection -> connection.setTransactionIsolation(connectionTransaction));
        session.doWork(
                connection -> log.info(String.valueOf(connection.getTransactionIsolation())));

        Map<String, String> variables = new HashMap<>();

        doInTransaction(em, () -> {
            Person personBeforeTx = em.find(Person.class, 1);
            variables.put(PERSON_BEFORE_TX, personBeforeTx.getName());

            doInTransaction(em2, () -> {
                Person personTx2 = em2.find(Person.class, 1, LockModeType.OPTIMISTIC);
                personTx2.setName(LocalDateTime.now().toString());
                variables.put(PERSON_TX2, personTx2.getName());
                em2.merge(personTx2);
                em2.flush();

                em.clear(); // entity is reloaded from DB - ignore 1th level cache
                Person personInTx = em.find(Person.class, 1);
                variables.put(PERSON_IN_TX, personInTx.getName());
            });

            em.clear(); // entity is reloaded from DB - ignore 1th level cache
            Person personAfterTx = em.find(Person.class, 1);
            variables.put(PERSON_AFTER_TX, personAfterTx.getName());
        });

        Person personTx3 = em3.find(Person.class, 1);
        variables.put(PERSON_TX3, personTx3.getName());

        canSee.forEach(key -> assertEquals(variables.get(PERSON_TX2), variables.get(key)));
        cannotSee.forEach(key -> assertNotEquals(variables.get(PERSON_TX2), variables.get(key)));
    }

    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

}
