package com.example.application.knowledge;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "know_department")
@Getter
@Setter
@NoArgsConstructor
public class Department implements IDepartment {
    @Id
    private int id;

    private String name;
    
    @OneToMany(
        cascade = CascadeType.ALL,  // TODO: asi nepotrebujem, staci iba mappedBy
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        mappedBy = "department"
    )
    private List<Person> persons;

    @Override
    public String toString() {
        return "Department"+ getId() + getJavaId();
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }
}
