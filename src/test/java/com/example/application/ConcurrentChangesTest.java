package com.example.application;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockException;
import com.example.application.knowledge.Person;
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
public class ConcurrentChangesTest {

    private static Logger log = LoggerFactory.getLogger(ConcurrentChangesTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    // TODO: OPTIMISTIC LOCKING

    @Test
    public void concurrentChangesTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            em.find(Person.class, 1, LockModeType.PESSIMISTIC_READ);

            doInTransaction(em2, () -> {
                assertDoesNotThrow(() -> em.find(Person.class, 1, LockModeType.PESSIMISTIC_READ));
                Person person2 = em2.find(Person.class, 1);
                person2.setName(LocalDateTime.now().toString());
                em2.merge(person2);
                assertThrows("message", PessimisticLockException.class, () -> em2.flush());
            });
        });
    }

    @Test
    public void concurrentChangesTest2() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            em.find(Person.class, 1, LockModeType.PESSIMISTIC_WRITE);

            doInTransaction(em2, () -> {
                assertThrows(PessimisticLockException.class, () -> em2.find(Person.class, 1, LockModeType.PESSIMISTIC_READ));
                Person person2 = em2.find(Person.class, 1);
                person2.setName(LocalDateTime.now().toString());
                em2.merge(person2);
                assertThrows(PessimisticLockException.class, () -> em2.flush());
            });
        });
    }

    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

}
