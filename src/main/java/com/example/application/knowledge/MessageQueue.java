package com.example.application.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import com.example.application.views.knowledge.LogType;

public class MessageQueue {

    private AtomicInteger counter = new AtomicInteger(0);
    private static MessageQueue instance;
    private List<Consumer<EventRow>> listeners = new ArrayList<>();

    public static synchronized MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }
        return instance;
    }

    public void add(LogType type, String object, String method, String payload) {
        EventRow event = new EventRow(type, counter.getAndIncrement(), object, method, payload);
        callListeners(event);
    }

    private void callListeners(EventRow text) {
        for (Consumer<EventRow> consumer : listeners) {
            consumer.accept(text);
        }
    }

    public void addListener(Consumer<EventRow> listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Consumer<EventRow> listener) {
        this.listeners.remove(listener);
    }
}
