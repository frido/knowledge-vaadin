package com.example.application.views.knowledge;

import java.time.LocalTime;

import com.example.application.knowledge.Department;
import com.example.application.knowledge.MessageQueue;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonWithVersion;
import com.example.application.knowledge.Team;
import com.example.application.services.EntityService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "card-list", layout = MainView.class)
@PageTitle("Card List")
public class KnowledgeView extends Div {

    private transient EntityService service;
    private transient MessageQueue messageQueue = MessageQueue.getInstance();
    private VerticalLayout infoPanel;
    private transient Person personEntity;
    private transient PersonWithVersion personWithVersionEntity;
    private transient Department departmentEntity;
    private transient Team teamEntity;
    private Label personLabel = new Label();
    private Label personWithVersionLabel = new Label();
    private Label departmentLabel = new Label();
    private Label teamLabel = new Label();

    public KnowledgeView(@Autowired EntityService service) { 
        this.service = service;

        var buttonPanel = new VerticalLayout();
        buttonPanel.add(personLabel, departmentLabel, teamLabel);
        
        var person1 = new HorizontalLayout();
        var loadPersonBtn = new Button("Load Person", this::onLoadPerson);
        var getDepartmentBtn = new Button("Get Department", this::onGetDepartment);
        var getTeamBtn = new Button("Get Team", this::onGetTeam);
        var person2 = new HorizontalLayout();
        var loatPersonTreeBtn = new Button("Load Person Tree", this::findPersonTree);
        var loatPersonFetchBtn = new Button("Load Person Fetch", this::findPersonFetch);
        person1.add(loadPersonBtn, getDepartmentBtn, getTeamBtn);
        person2.add(loatPersonTreeBtn, loatPersonFetchBtn);
        buttonPanel.add(person1, person2);

        var merge1 = new HorizontalLayout();
        var merge2 = new HorizontalLayout();
        var merge3 = new HorizontalLayout();
        var merge4 = new HorizontalLayout();
        var mergePersonBtn = new Button("Merge Person", this::onMergePerson);
        var mergePersonAllBtn = new Button("Merge Person All", this::onMergePersonAll);
        var mergePersonDtoBtn = new Button("Merge Person DTO", this::onMergePersonDto);
        var loadPersonVersionBtn = new Button("Load Person Version", this::onLoadPersonVerson);
        var changePersonVersionBtn = new Button("Change Person Version", this::onChangePersonVerson);
        var mergePersonVersionBtn = new Button("Merge Person Version", this::onMergePersonVerson);
        var editPersonInServiceBtn = new Button("Edit Person In Service", this::onEditPersonInService);
        var editPersonOutServiceBtn = new Button("Edit Person Out Service", this::onEditPersonOutService);
        var editAllPersons = new Button("Edit All Persons", this::onEditAllPersons);
        var editAllPersonsBatch = new Button("Edit All Persons Batch", this::onEditAllPersonsBatch);
        merge1.add(mergePersonBtn, mergePersonAllBtn, mergePersonDtoBtn);
        merge2.add(loadPersonVersionBtn, changePersonVersionBtn, mergePersonVersionBtn);
        merge3.add(editPersonInServiceBtn, editPersonOutServiceBtn);
        merge4.add(editAllPersons, editAllPersonsBatch);
        buttonPanel.add(merge1, personWithVersionLabel, merge2, merge3, merge4);

        // TODO: spring properties show_sql, hibernate_statistics, batch_size
        // TODO: why more entity managers, preco ma ten v pm1 meno a cim sa lisi od ostatnycb
        // TODO: transaction isolation, sesions (httpSession, VaadinSession, SpringSession)?
        // TODO: splitnut projekt na generovanie a knowledge
        // TODO: kde sa inicializuje VaadinServlet?
        // TODO: ine formy optimistic lock (version) rieseni
        // TODO: java packages dependency grapgh
        // TODO: routing stranok
        // TODO: Authorizacia usera
        // TODO: Cachovanie master data, alebo cokolvek ine
        // TODO: Genericke filtrovanie/sortovanie gridov

        infoPanel = new VerticalLayout();
        infoPanel.setPadding(false);
        infoPanel.setSpacing(false);

        var main = new HorizontalLayout();
        main.add(buttonPanel, infoPanel);
        add(main);

        messageQueue.addListener(x -> infoPanel.addComponentAtIndex(0, new Label(x)));
    }

    private void onLoadPerson(ClickEvent<Button> event) {
        clean();
        personEntity = service.find(Person.class);
        personLabel.setText(String.valueOf(personEntity));
    }

    private void onMergePerson(ClickEvent<Button> event) {
        clean();
        personEntity.setName(randomText());
        personEntity = service.merge(personEntity);
        personLabel.setText(String.valueOf(personEntity));
    }

    private void onEditPersonInService(ClickEvent<Button> event) {
        clean();
        personEntity = service.findAndEdit();
        personLabel.setText(String.valueOf(personEntity));
    }

    private void onEditPersonOutService(ClickEvent<Button> event) {
        clean();
        personEntity = service.find(Person.class);
        personEntity.setName("edited out service");
        personLabel.setText(String.valueOf(personEntity));
    }

    private void onEditAllPersons(ClickEvent<Button> event) {
        clean();
        service.onEditAllPersons();
    }

    private void onEditAllPersonsBatch(ClickEvent<Button> event) {
        clean();
        service.onEditAllPersonsBatch();
    }

    private void onMergePersonAll(ClickEvent<Button> event) {
        clean();
        personEntity.setName(randomText());
        personEntity.getDepartment().setName(randomText());
        personEntity.getTeam().setName(randomText());
        personEntity = service.merge(personEntity);
        personLabel.setText(String.valueOf(personEntity));
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

    private void onLoadPersonVerson(ClickEvent<Button> event) {
        personWithVersionEntity = service.findPersonWithVersion();
        personWithVersionLabel.setText(String.valueOf(personWithVersionEntity));
    }

    private void onChangePersonVerson(ClickEvent<Button> event) {
        var person = service.findPersonWithVersion();
        person.setName(randomText());
        service.merge(person);
    }

    private void onMergePersonVerson(ClickEvent<Button> event) {
        try {
        personWithVersionEntity.setName(randomText());
        service.merge(personWithVersionEntity);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private void findPersonTree(ClickEvent<Button> event) {
        clean();
        personEntity = service.findPersonTree();
        teamEntity = personEntity.getTeam();
        departmentEntity = personEntity.getDepartment();
        personLabel.setText(String.valueOf(personEntity));
        teamLabel.setText(String.valueOf(teamEntity));
        departmentLabel.setText(String.valueOf(departmentEntity));
    }

    private void findPersonFetch(ClickEvent<Button> event) {
        clean();
        personEntity = service.findPersonFetch();
        teamEntity = personEntity.getTeam();
        departmentEntity = personEntity.getDepartment();
        personLabel.setText(String.valueOf(personEntity));
        teamLabel.setText(String.valueOf(teamEntity));
        departmentLabel.setText(String.valueOf(departmentEntity));
    }
    

    private void onGetDepartment(ClickEvent<Button> event) {
        departmentEntity = personEntity.getDepartment();
        departmentLabel.setText(String.valueOf(departmentEntity));
    }

    private void onGetTeam(ClickEvent<Button> event) {
        teamEntity = personEntity.getTeam();
        service.run(() -> teamLabel.setText(String.valueOf(teamEntity)));
    }

    private void clean() {
        personLabel.setText("");
        departmentLabel.setText("");
        teamLabel.setText("");
    }

    private String randomText() {
        return LocalTime.now().toString();
    }

}