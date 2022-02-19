package com.example.application.knowledge;

public record PersonDtoRecord(
    int id,
    String name,
    Department department,
    Team team
) {
}