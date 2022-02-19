package com.example.application.knowledge;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "know_car")
@Immutable
public class Car {
    @Id
    private int id;
    private String spz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpz() {
        return this.spz;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", spz='" + getSpz() + "'" +
            ", person='" + getPerson() + "'" +
            "}";
    }


    
}
