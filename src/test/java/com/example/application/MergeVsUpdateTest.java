package com.example.application;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.example.application.knowledge.EventRow;
import com.example.application.knowledge.MessageQueue;
import com.example.application.knowledge.PersonWithVersion;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JPA merge vs Hibernate update See tests.
 * 
 * JPA persist vs Hibernate save In almost all situations, Hibernate generates the primary key value
 * immediately and triggers an SQL statement if necessary, when you call the persist or save method.
 * But that is not the case if you use the IDENTITY strategy and try to persist an entity without an
 * active transaction or with FlushMode.MANUAL. If you call the persist method in one of these
 * situations, Hibernate delays the execution of the SQL INSERT statement and creates a temporary
 * primary key value. But if you call the save method, Hibernate performs the SQL INSERT statement
 * immediately and retrieves the primary key value from the database.
 * 
 * https://thorben-janssen.com/persist-save-merge-saveorupdate-whats-difference-one-use/
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@ComponentScan(value = "com.example.application")
public class MergeVsUpdateTest implements Consumer<EventRow> {

    private static Logger log = LoggerFactory.getLogger(MergeVsUpdateTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    MessageQueue queue = MessageQueue.getInstance();
    List<EventRow> list = new ArrayList<>();

    @Before
    public void before() {
        queue.addListener(this);
    }

    @After
    public void after() {
        queue.removeListener(this);
        list.clear();
    }

    /**
     * Merge copy attributes from detached entity (entity1) to managed entity (entity2)
     */
    @Test
    public void testJpaMerge2() {
        EntityManager em = entityManagerFactory.createEntityManager();
        doInTransaction(em, () -> {
            // create detached entity with name1
            var entity1 = em.find(PersonWithVersion.class, 1);
            var name1 = LocalDateTime.now().toString();
            entity1.setName(name1);
            em.detach(entity1);

            // create managed entity with name2
            var entity2 = em.find(PersonWithVersion.class, 1);
            var name2 = LocalDateTime.now().toString();
            entity2.setName(name2);

            // copy attributes from detached entity (entity1) to managed entity (entity2)
            em.merge(entity1);

            // managed entity has name from detached entity
            assertEquals(entity2.getName(), name1);
        });
    }

    /**
     * Merge copy attributes from detached entity (entity1) to managed entity (entity2)
     */
    @Test
    public void testHibernateUpdate2() {
        EntityManager em = entityManagerFactory.createEntityManager();
        doInTransaction(em, () -> {
            // create detached entity with name1
            Session session = em.unwrap(Session.class);
            var entity1 = session.find(PersonWithVersion.class, 1);
            var name1 = LocalDateTime.now().toString();
            entity1.setName(name1);
            session.detach(entity1);

            // create managed entity with name2
            var entity2 = session.find(PersonWithVersion.class, 1);
            var name2 = LocalDateTime.now().toString();
            entity2.setName(name2);

            // can not overrite managed entity
            assertThrows(NonUniqueObjectException.class, () -> session.update(entity1));
        });
    }


    /**
     * Merge calls sql to load entity to calculate dirty state
     */
    @Test
    public void testJpaMerge() {
        EntityManager em = entityManagerFactory.createEntityManager();
        doInTransaction(em, () -> {
            var entity = em.find(PersonWithVersion.class, 1); // onPrepareStatement - load entity
            em.detach(entity);
            em.merge(entity); // onPrepareStatement - load entity to check dirty state
        });
        // one from find, second from merge
        assertEquals(2, filterSql("from know_person_with_version"));
        // no update because it is not dirty
        assertEquals(0, filterSql("update know_person_with_version"));
    }

    /**
     * Update don't load entity to calculate dirty state, but entity is automatically marked as
     * dirty
     */
    @Test
    public void testHibernateUpdate() {
        EntityManager em = entityManagerFactory.createEntityManager();
        doInTransaction(em, () -> {
            Session session = em.unwrap(Session.class);
            var entity = session.find(PersonWithVersion.class, 1); // onPrepareStatement - load
                                                                   // entity
            session.detach(entity);
            session.update(entity); // no load entity. To load entity for dirty check - set
                                    // @SelectBeforeUpdate
        });
        // one from find
        assertEquals(1, filterSql("from know_person_with_version"));
        // one from update entity because it was not loaded so marked it as dirty
        assertEquals(1, filterSql("update know_person_with_version"));
        // printLogs();
    }

    @Override
    public void accept(EventRow row) {
        list.add(row);
    }

    private void doInTransaction(EntityManager em, Runnable action) {
        em.getTransaction().begin();
        action.run();
        em.getTransaction().commit();
    }

    private long filterSql(String sqlPart) {
        return filterLogs("onPrepareStatement", sqlPart).size();
    }

    private List<EventRow> filterLogs(String method, String payload) {
        return this.list.stream().filter(row -> {
            if (method == null || row.getMethod().contains(method)) {
                if (payload == null
                        || (row.getPayload() != null && row.getPayload().contains(payload))) {
                    return true;
                }
            }
            return false;
        }).toList();
    }

    private void printLogs() {
        this.list.forEach(x -> log.info(x.toString()));
    }
}
