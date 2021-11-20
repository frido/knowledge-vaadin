package com.example.application.knowledge;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "know_item")
public class Item {
    @Id
    private int id;
    private String name;

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
        return "Item " + getJavaId() + " {" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }

}
