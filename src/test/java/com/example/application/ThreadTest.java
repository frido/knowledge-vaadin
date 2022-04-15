package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadLocal store value specified for current Thread. In this way I can be sure about thread-safe.
 * Each Thread can see own value.
 * 
 * NOTE: can be dangerous for thread pool, because it "reause" Threads.
 * NOTE: Thread cannot be reused (we can not call start() twice)
 * It is reaused that pool of threads are waiting for Runnable and first free Thread run() it.
 * 
 */
public class ThreadTest {

    private static Logger log = LoggerFactory.getLogger(ThreadTest.class);

    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    CountDownLatch latch;
    
/**
 * MyRunnable runs 2 tasks: 1. write value to the ThreadLocal, 2. read value from ThreadLocal.
 * There is sleep in between tasks.
 * Test checks that write tasks and read tasks have the same value.
 * 
 * @throws InterruptedException
 */
    @Test
    public void multiThread() throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        List<Pair<WriteTask, ReadTask>> pairs = new ArrayList<>();

        for (int i = 0 ; i<10 ; i++) {
            Pair<WriteTask, ReadTask> pair = createCouple(i);
            MyRunnable runnable = new MyRunnable(pair.getLeft(), pair.getRight());
            pairs.add(pair);
            threads.add(new Thread(runnable));
        }

        latch = new CountDownLatch(threads.size());

        threads.forEach(Thread::start);

        latch.await();

        pairs.forEach(r -> assertEquals(r.getLeft().getValue(), r.getRight().getValue()));
    }

    /**
     * create couple (pair) of Write task and Read task
     * @param i
     * @return
     */
    private Pair<WriteTask, ReadTask> createCouple(int i) {
        return new ImmutablePair<>(new WriteTask(i), new ReadTask());
    }

    /**
     * Run write task, wait random time, run read task
     */
    private class MyRunnable implements Runnable {

        WriteTask writeTask;
        ReadTask readTask;

        MyRunnable(WriteTask writeTask, ReadTask readTask) {
            this.writeTask = writeTask;
            this.readTask = readTask;
        }

        @Override
        public void run() {
            writeTask.run();
            Awaitility.await().between(100, TimeUnit.MILLISECONDS, 200, TimeUnit.MILLISECONDS);  
            readTask.run();
            latch.countDown();
        }
    }

    interface Task extends Runnable {}

    /**
     * write to the ThreadLocal
     */
    private class WriteTask implements Task {

        private Integer value;

        WriteTask(Integer value) {
            this.value = value;
        }

        @Override
        public void run() {
            threadLocal.set(value);
            log.info("set value: {}", value);
        }

        public Integer getValue() {
            return value;
        }

    }

    /**
     * Read from the ThreadLocal
     */
    private class ReadTask implements Task {

        private Integer value;

        @Override
        public void run() {
            value = threadLocal.get();
            log.info("read value: {}", value);
        }

        public Integer getValue() {
            return value;
        }

    }
}
