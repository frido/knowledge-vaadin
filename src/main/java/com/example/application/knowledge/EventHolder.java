package com.example.application.knowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHolder {

    Map<String, Object> props = new HashMap<>();

    public void add(String string, int value) {
        props.put(string, value);
    }

    @Override
    public String toString() {
        List<String> params = props.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()).toList();
        return String.join(",", params);
    }

}
