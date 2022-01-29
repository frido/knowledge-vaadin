package com.example.application.knowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EventHolder {

    Map<String, Object> props = new HashMap<>();

    public void add(String string, int value) {
        props.put(string, value);
    }

    @Override
    public String toString() {
        List<String> params = props.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList());
        return String.join(",", params);
    }
    
}
