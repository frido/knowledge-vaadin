package com.example.application.knowledge;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "know_item")
public class Item {
    @Id
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Person> persons;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item"+ getId() + getJavaId();
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
