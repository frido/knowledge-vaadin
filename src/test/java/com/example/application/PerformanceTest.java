package com.example.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.example.application.performance.MyPerformance;
import com.example.application.performance.MyPerformanceBean;
import com.example.application.performance.MyPerformanceDoubleBean;
import com.example.application.performance.MyPerformanceTransactionalBean;
import com.example.application.performance.MyPerformanceTransactionalNestedBean;
import com.example.application.performance.MyPerformanceNestedBean;
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
    MyPerformanceTransactionalBean myPerformanceTransactionalBean;

    @Autowired
    MyPerformanceTransactionalNestedBean myPerformanceTransactionalNestedBean;

    @Autowired
    MyPerformanceDoubleBean myPerformanceDoubleBean;

    @Autowired
    MyPerformanceBean myPerformanceBean;

    /**
     * EJB call is much slower. There are interceptors, and long stack in debug that invoke target class
     * @Transactional annotation slowing down the calling. It creates proxy around bean to start/stop transaction.
     * 
     */
    @Test
    public void ejb() {
        MyPerformance nobean = new MyPerformance();
       
        /**
         * Transactional method call next service with transactional method.
         * Around transactional methods (both) is proxy that check transactions.
         * Even if transaction is not needed or created the proxy is still there.
         * This is the slowest call.
         */
        long watch5 = watch(() -> myPerformanceTransactionalNestedBean.getterTransactionalNested());

        /**
         * Transactional method call own transactional method.
         * Method called from own service (even it is transactional) is called directly.
         * So there is only one Transactional proxy around main method, 
         * methods called from inside service are called directly.
         */
        long watch4 = watch(() -> myPerformanceDoubleBean.getterTransactionalDouble());

        /**
         * Transactional method call nothing, but there is still transactional proxy around method.
         */
        long watch3 = watch(() -> myPerformanceTransactionalBean.getterTransactional());

        /**
         * Method without transactional annotation is called directly even it is injected object.
         * Injected object is exact object created in the configuration (without any proxy)
         */
        long watch2 = watch(() -> myPerformanceBean.getter());

        /**
         * Direct call to the object.
         */
        long watch1 = watch(() -> nobean.getter());

        System.out.println("nobean.getter - " + watch1);
        System.out.println("bean.getter - " + watch2);
        System.out.println("bean.getterTransactional - " + watch3);
        System.out.println("bean.getterTransactionalDouble - " + watch4);
        System.out.println("bean.getterTransactionalNested - " + watch5);

        assertTrue(watch1 < watch5);
    }

    private long watch(Runnable runnable) {
        StopWatch w = new StopWatch();
        w.start();
        for(int i = 0 ; i < 100 ; i++) {
            runnable.run();
        }
        w.stop();
        return w.getTime();
    }
}
