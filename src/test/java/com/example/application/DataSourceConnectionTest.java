package com.example.application;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.example.application.knowledge.PersonWithVersion;
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
public class DataSourceConnectionTest {

    private static Logger log = LoggerFactory.getLogger(DataSourceConnectionTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MyMetricsTrackerFactory trackerFactory;

    /**
     * Test connections of application managed EntityManager
     * 
     * Container-managed entityManager is
     * @PersistenceContext
     * private EntityManager entityManager;
     * The container ensures that each EntityManager is confined to one thread.
     * 
     * @throws InterruptedException
     */
    @Test
    public void applicationManagedEntityManager() throws InterruptedException {

        List<EntityManager> ems = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++) {
            ems.add(entityManagerFactory.createEntityManager());
        }
        for (EntityManager em : ems) {
            em.find(PersonWithVersion.class, 1); // make query to create connection
        }

        Thread.sleep(1000); // because PoolStats refresh stats max 1 per second TODO: use await
        var x1a = trackerFactory.getPoolStats().getActiveConnections(); // TODO: encapsulate to nice object
        assertEquals(10, x1a);

        // There is 10 connections
        // Get 11th connection should failed
        assertThrows(Exception.class, () -> entityManagerFactory.createEntityManager().getTransaction().begin());
        
        ems.get(0).close(); // close entity manager

        // so there should be only 9 connections
        var x1c = trackerFactory.getPoolStats().getActiveConnections();
        assertEquals(9, x1c);

        // Now it is possible to create new EntityManager
        entityManagerFactory.createEntityManager().getTransaction().begin();
    }
}
