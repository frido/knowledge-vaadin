package com.example.application;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PessimisticLockException;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonWithVersion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@ComponentScan(value = "com.example.application")
public class ConcurrentChangesTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * LockModeType.PESSIMISTIC_READ
     */
    @Test
    public void concurrentChangesTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            em.find(Person.class, 1, LockModeType.PESSIMISTIC_READ); // Lock READ = shared lock

            doInTransaction(em2, () -> {
                assertDoesNotThrow(() -> em.find(Person.class, 1, LockModeType.PESSIMISTIC_READ)); // I can have more shared lockes on record
                Person person2 = em2.find(Person.class, 1); // I can load wihtout locking
                person2.setName(LocalDateTime.now().toString());
                em2.merge(person2);
                assertThrows("message", PessimisticLockException.class, () -> em2.flush()); // Record is locked - can not be changed
            });
        });
    }

    /**
     * LockModeType.PESSIMISTIC_WRITE
     */
    @Test
    public void concurrentChangesTest2() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            em.find(Person.class, 1, LockModeType.PESSIMISTIC_WRITE); // Load WRITE = exclusive lock

            doInTransaction(em2, () -> {
                assertThrows(PessimisticLockException.class, () -> em2.find(Person.class, 1, LockModeType.PESSIMISTIC_READ)); // I can not lock because of there is exclusive lock
                Person person2 = em2.find(Person.class, 1); // I can load without locking
                person2.setName(LocalDateTime.now().toString());
                em2.merge(person2);
                assertThrows(PessimisticLockException.class, () -> em2.flush()); // Record is locked - can not be changed
            });
        });
    }

    /**
     * Optimistick locking - Entity with Version
     */
    @Test
    public void concurrentChangesTest3() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            PersonWithVersion person1 = em.find(PersonWithVersion.class, 1);

            doInTransaction(em2, () -> {
                PersonWithVersion person2 = em2.find(PersonWithVersion.class, 1);
                person2.setName(LocalDateTime.now().toString()); // Change so version is increased
                // em2.merge(person2); // not needed, then auto-flush
            });

            person1.setName(LocalDateTime.now().toString());
            em.merge(person1);
            assertThrows(OptimisticLockException.class, () -> em.flush()); // Can not update because of version was already increased
        });
    }

    @Test
    public void concurrentChangesTest4() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            PersonWithVersion person1 = em.find(PersonWithVersion.class, 1);

            doInTransaction(em2, () -> {
                PersonWithVersion person2 = em2.find(PersonWithVersion.class, 1);
                em2.merge(person2); // dont change so version should keep old value
            });

            person1.setName(LocalDateTime.now().toString());
            em.merge(person1);
            assertDoesNotThrow(() -> em.flush()); // Can update because of version was not changed
        });
    }

    @Test
    public void concurrentChangesTest5() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        doInTransaction(em, () -> {
            PersonWithVersion person1 = em.find(PersonWithVersion.class, 1);

            doInTransaction(em2, () -> {
                em2.find(PersonWithVersion.class, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                // em2.merge(person2); // not needed, then auto-flush
            }); // version is increased, althou entity was not changed

            person1.setName(LocalDateTime.now().toString());
            em.merge(person1);
            assertThrows(OptimisticLockException.class, () -> em.flush()); // Can not update because of version was already increased
        });
    }

    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

}
