package com.example.application.annotation;

@MyAnnotation("TABLE_1")
public class MyAnnotatedObject {

    @MyAnnotation("COL_1")
    private String field1;

    @MyAnnotation("COL_2")
    private String field2;
}
