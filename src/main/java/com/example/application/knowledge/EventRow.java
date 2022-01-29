package com.example.application.knowledge;

public class EventRow {
    int id;
    String object;
    String method;
    String payload;

    public EventRow(int id, String object, String method, String payload) {
        this.id = id;
        this.object = object;
        this.method = method;
        this.payload = payload;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
