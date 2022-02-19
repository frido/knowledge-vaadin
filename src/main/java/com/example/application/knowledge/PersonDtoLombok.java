package com.example.application.knowledge;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PersonDtoLombok {
    private int id;
    private String name;
    private Department department;
    private Team team;
}
