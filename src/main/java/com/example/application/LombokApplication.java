package com.example.application;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class LombokApplication {

    public static void main(String[] args) {
        Pojo.PojoBuilder builder = Pojo.builder();
        builder.id(1).name("Peter").age(36);
        System.out.println(builder.build());
    }

    @Getter
    @Builder
    @ToString
    static class Pojo {
        int id;
        String name;
        int age;
    }
}
