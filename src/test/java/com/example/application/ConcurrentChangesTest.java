package com.example.application;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
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

    /**
     * TODO: Concurent changes (locks)
     */
    @Test
    public void concurrentChangesTest() {
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


    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

}
