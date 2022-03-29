package com.example.application.knowledge;

import com.example.application.views.knowledge.LogType;

public class EventRow {
    LogType type;
    int id;
    String object;
    String method;
    String payload;

    public EventRow(LogType type, int id, String object, String method, String payload) {
        this.type = type;
        this.id = id;
        this.object = object;
        this.method = method;
        this.payload = payload;
    }

    public LogType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public String getObject() {
        return this.object;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPayload() {
        return this.payload;
    }

    @Override
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", id='" + getId() + "'" +
            ", object='" + getObject() + "'" +
            ", method='" + getMethod() + "'" +
            ", payload='" + getPayload() + "'" +
            "}";
    }


}
