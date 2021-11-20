package com.example.application.knowledge;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "know_team")
public class Team {
    @Id
    private int id;
    private String name;
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "team"
    )
    private List<Person> persons;

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

    public List<Person> getPersons() {
        return this.persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String  toString() {
        return "Team " + getJavaId() + " {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }

}
