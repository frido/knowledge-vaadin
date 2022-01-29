package com.example.application.views.knowledge;

import com.example.application.knowledge.*;
import com.example.application.services.EntityService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class SimplePersonView extends Div {

    private final transient EntityService service;

    private transient Person personEntity;

    private final Label personLabel = new Label();

    public SimplePersonView(EntityService service) {
        this.service = service;
        add(personLabel);
        var loadPersonBtn = new Button("Load Person", this::onLoadPerson);
        var loadPersonTreeBtn = new Button("Load Person Tree", this::findPersonTree);
        var loadPersonFetchBtn = new Button("Load Person Fetch", this::findPersonFetch);
        var mergePersonBtn = new Button("Merge Person", this::onMergePerson);
        var mergePersonAllBtn = new Button("Merge Person All", this::onMergePersonAll);
        var mergePersonDtoBtn = new Button("Merge Person DTO", this::onMergePersonDto);
        add(new Div(loadPersonBtn));
        add(new Div(loadPersonFetchBtn, loadPersonTreeBtn));
        add(new Div(mergePersonBtn, mergePersonAllBtn, mergePersonDtoBtn));
    }

    private void onLoadPerson(ClickEvent<Button> event) {
        clean();
         personEntity = service.find(Person.class);
//        personEntity = service.testing3();
        displayPerson(personEntity);

    }

    private void displayPerson(Person person) {
        personLabel.setText(personToString(person));
    }

    private void findPersonTree(ClickEvent<Button> event) {
        clean();
        personEntity = service.findPersonTree();
        displayPerson(personEntity);
    }

    private void findPersonFetch(ClickEvent<Button> event) {
        clean();
        personEntity = service.findPersonFetch();
        displayPerson(personEntity);
    }

    private void onMergePerson(ClickEvent<Button> event) {
        clean();
        personEntity.setName(randomText());
        personEntity = service.merge(personEntity);
        displayPerson(personEntity);
    }

    private void onMergePersonAll(ClickEvent<Button> event) {
        clean();
        personEntity.setName(randomText());
        personEntity.getDepartment().setName(randomText());
        personEntity.getTeam().setName(randomText());
        personEntity = service.merge(personEntity);
        displayPerson(personEntity);
    }

    private void onMergePersonDto(ClickEvent<Button> event) {
        clean();
        var personDto = service.findPersonDto();
        var personEnt = new Person();
        personEnt.setId(personDto.id());
        personEnt.setName(randomText());
        personEnt.setDepartment(personDto.department());
        personEnt.setTeam(personDto.team()); // TODO: nie uplne spravne DTO kedze toto je proxy
        personEnt = service.merge(personEnt);
        personLabel.setText(String.valueOf(personEnt));
    }

    private void clean() {
        personLabel.setText("");
    }

    private String randomText() {
        return LocalTime.now().toString();
    }

    public String personToString(Person person) {
        return "Person " + "{" +
                " id='" + getId() + "'" +
                ", name='" + person.getName() + "'" +
                ", department='" + getDepartmentString(person) + "'" +
                ", team='" + getTeamString(person) + "'" +
                ", items='" + getItemsString(person) + "'" +
                "}";
    }

    private String getDepartmentString(Person person) {
        try {
            return String.valueOf(person.getDepartment());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getTeamString(Person person) {
        try {
            return String.valueOf(person.getTeam());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getItemsString(Person person) {
        try {
            return itemsToString(person);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String itemsToString(Person person) {
        String str = "";
        for (Item item : person.getItems()) {
            str = str + String.valueOf(item) + ", ";
        }
        return str;
    }


}
