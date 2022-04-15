package com.example.application.reflection;

public class MyReflectionObject extends MySuperReflectionObject implements MyReflectionInterface {
    
    public static final String FIELD_2 = "peter";
    
    private String field2 = FIELD_2;

    public MyReflectionObject() {
        super();
    }

    @Override
    public String getField2() {
        return field2;
    }
}
