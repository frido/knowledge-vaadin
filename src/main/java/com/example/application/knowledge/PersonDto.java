package com.example.application.knowledge;

public record PersonDto(
    int id,
    String name,
    Department department,
    Team team
) {
}