package com.example.application.knowledge;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "know_person")
public class Person {
    @Id
    private int id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "know_person_item", 
        joinColumns = { @JoinColumn(name = "person_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "item_id") }
    )
    private Set<Item> items;
    @OneToMany(fetch = FetchType.LAZY, mappedBy="person") 
    private List<Car> cars;

    public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

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


    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


    public List<Car> getCars() {
        return this.cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }


    // FIXME: nemozem to vsetko takto vypisovat v toString, pretoze entitu vypisujem v interceptoroch a tym padom sa mi nacitavaju lazy veci aj ked nechcem
    @Override
    public String toString() {
        return "Person " + getJavaId() + "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
//                ", department='" + getDepartmentString() + "'" +
//                ", team='" + getTeamString() + "'" +
//                ", items='" + getItemsString() + "'" +
                "}";
    }

    private String getJavaId() {
        return super.toString().substring(super.toString().indexOf("@"));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @PostLoad
    public void postLoad() {
//        TODO: generic base class with all listeners
    }
}
