package com.example.application.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MessageQueue {

    private AtomicInteger counter = new AtomicInteger(0);
    private static MessageQueue INSTANCE;
    private LinkedBlockingQueue<EventRow> queue = new LinkedBlockingQueue<>();
    private List<Consumer<EventRow>> listeners = new ArrayList<>();

    public static synchronized MessageQueue getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageQueue();
        }
        return INSTANCE;
    }

    // public List<EventRow> poolAll() {
    //     List<EventRow> list = new ArrayList<>();
    //     queue.forEach(list::add);
    //     queue.clear();
    //     Collections.reverse(list);
    //     return list;
    // }

    public void add(String object, String method, String payload) {
        EventRow event = new EventRow(counter.getAndIncrement(), object, method, payload);
        callListeners(event);
    }

    private void callListeners(EventRow text) {
        for (Consumer<EventRow> consumer : listeners) {
            consumer.accept(text);
        }
    }

    public void addListener(Consumer<EventRow> listener) {
        queue.forEach(listener);
        this.listeners.add(listener);
    }
    
}
