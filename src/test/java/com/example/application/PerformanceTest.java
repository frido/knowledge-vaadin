package com.example.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.example.application.performance.MyPerformance;
import com.example.application.performance.MyPerformanceBean;
import com.example.application.performance.MyPerformanceSessionBean;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO: From book Java Performance, The Definitive Guide
 * 
 * heap size = yong generation + old generation
 * permgen/metaspace = hold class metadata (for jvm)
 * 
 * TODO: see https://dzone.com/articles/understanding-the-java-memory-model-and-the-garbag
 * 
 * see {@code WeakReferenceTest.java}
 * 
 * TODO: serializable
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class PerformanceTest {

    @Autowired
    MyPerformanceBean bean;

    /**
     * EJB call is much slower. There are interceptors, and long stack in debug that invoke target class
     * @Transactional annotation slowing down the calling. It creates proxy around bean to start/stop transaction.
     */
    @Test
    public void ejb() {
        MyPerformance nobean = new MyPerformance();

        StopWatch watch1 = new StopWatch();
        watch1.start();
        for(int i = 0 ; i < 1000 ; i++) {
            nobean.getter();
        }
        watch1.stop();

        StopWatch watch2 = new StopWatch();
        watch2.start();
        for(int i = 0 ; i < 1000 ; i++) {
            bean.getter(); // This is service wrapped in the Proxy so event not transaction, it is little slower
        }
        watch2.stop();

        StopWatch watch3 = new StopWatch();
        watch3.start();
        for(int i = 0 ; i < 1000 ; i++) {
            bean.getterTransactional(); // @Transactional add proxy around bean
        }
        watch3.stop();

        System.out.println("1 - " + watch1.getTime());
        System.out.println("2 - " + watch2.getTime());
        System.out.println("3 - " + watch3.getTime());

        assertTrue(watch1.getTime() < watch2.getTime());
        assertTrue(watch2.getTime() < watch3.getTime());
    }
}
