package com.example.application.knowledge;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "know_department")
public class Department {
    @Id
    private int id;
    private String name;
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        mappedBy = "department"
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

    @Override
    public String toString() {
        return "Department " + getJavaId() + " {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }

}
