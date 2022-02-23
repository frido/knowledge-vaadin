package com.example.application.views.knowledge;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.example.application.knowledge.EventRow;
import com.example.application.knowledge.MessageQueue;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonWithVersion;
import com.example.application.services.EntityService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hibernate.annotations.Immutable;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainView.class)
@PageTitle("Card List")
public class KnowledgeView extends Div {

    private transient EntityService service;
    private transient MessageQueue messageQueue = MessageQueue.getInstance();
    private VerticalLayout infoPanel;
    private transient Person personEntity;
    private transient PersonWithVersion personWithVersionEntity;
    private Label personLabel = new Label();
    private Label personWithVersionLabel = new Label();
    private VerticalLayout buttonPanel;

    Grid<EventRow> grid = new Grid<>(EventRow.class);
    transient List<EventRow> items = new ArrayList<>();
    CheckboxGroup<LogType> checkboxGroup = new CheckboxGroup<>();
    
    private UI ui;

    public KnowledgeView(@Autowired EntityService service) { 
        this.service = service;

        checkboxGroup.setItems(LogType.values());

        buttonPanel = new VerticalLayout();
        buttonPanel.add(new SimplePersonView(service));

        var merge1 = new HorizontalLayout();
        var merge2 = new HorizontalLayout();
        var merge3 = new HorizontalLayout();
        var merge4 = new HorizontalLayout();
        var merge5 = new HorizontalLayout();
        var loadPersonVersionBtn = new Button("Load Person Version", this::onLoadPersonVersion);
        var changePersonVersionBtn = new Button("Change Person Version", this::onChangePersonVersion);
        var mergePersonVersionBtn = new Button("Merge Person Version", this::onMergePersonVersion);
        var editPersonInServiceBtn = new Button("Edit Person In Service", this::onEditPersonInService);
        var editPersonOutServiceBtn = new Button("Edit Person Out Service", this::onEditPersonOutService);
        var editAllPersons = new Button("Edit All Persons", this::onEditAllPersons);
        var editAllPersonsBatch = new Button("Edit All Persons Batch", this::onEditAllPersonsBatch);
        var addCar = new Button("Add car", this::onAddCar);
        var clearPersistentContext = new Button("Clear", this::clearPersistentContext);
        var reaOnlyTransaction = new Button("reaOnlyTransaction", this::reaOnlyTransaction);
        

        var testing = new Button("Testing", this::testing);

        merge2.add(loadPersonVersionBtn, changePersonVersionBtn, mergePersonVersionBtn);
        merge3.add(editPersonInServiceBtn, editPersonOutServiceBtn);
        merge4.add(editAllPersons, editAllPersonsBatch);
        merge5.add(addCar);
        merge5.add(clearPersistentContext);
        merge5.add(reaOnlyTransaction);
        buttonPanel.add(merge1, personWithVersionLabel, merge2, personLabel, merge3, merge4, testing, merge5);

        infoPanel = new VerticalLayout();
        infoPanel.setPadding(false);
        infoPanel.setSpacing(false);

        var main = new HorizontalLayout();
        buttonPanel.setWidth(500, Unit.PIXELS);
        main.add(buttonPanel, infoPanel);
        add(main);

        grid.setColumns("id", "object", "method", "payload");
        grid.getColumns().forEach(x -> x.setAutoWidth(true));
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItemDetailsRenderer(TemplateRenderer.<EventRow>of("[[item.payload]]").withProperty("payload", EventRow::getPayload));
        var clearGridBtn = new Button("Clear", this::onClearGrid);
        infoPanel.add(clearGridBtn, checkboxGroup, grid);

        messageQueue.addListener(this::onNewMessage);

        Button tBtn = new Button("Thread");

        tBtn.addClickListener( x -> {
            new Thread(() -> {
                ui.access(() -> {
                    tBtn.setEnabled(false);
                });
                try {
                    service.testing2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                ui.access(() -> {
                    tBtn.setEnabled(true);
                });
            }).start();
        });
        buttonPanel.add(tBtn);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui = attachEvent.getUI();
    }

    private void onNewMessage(EventRow event) {
        ui.access(() -> {
            if (!checkboxGroup.getSelectedItems().contains(event.getType())) {
                return;
            }
            items.add(0, event);

            grid.setItems(items);
            grid.getDataProvider().refreshAll();
        });
    }

    private void onClearGrid(ClickEvent<Button> event) {
        items.clear();
        grid.setItems(items);
        grid.getDataProvider().refreshAll();
    }

    private void testing(ClickEvent<Button> event) {
        service.testing();
    }

    @Immutable
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
        service.merge(personEntity);
    }

    private void onEditAllPersons(ClickEvent<Button> event) {
        clean();
        service.onEditAllPersons();
    }

    private void onAddCar(ClickEvent<Button> event) {
        clean();
        service.onAddCarToPerson();
    }

    private void clearPersistentContext(ClickEvent<Button> event) {
        service.clearPersistentContext();
    }

    private void reaOnlyTransaction(ClickEvent<Button> event) {
        service.reaOnlyTransaction();
    }

    private void onEditAllPersonsBatch(ClickEvent<Button> event) {
        clean();
        service.onEditAllPersonsBatch();
    }

    private void onLoadPersonVersion(ClickEvent<Button> event) {
        personWithVersionEntity = service.findPersonWithVersion();
        personWithVersionLabel.setText(String.valueOf(personWithVersionEntity));
    }

    private void onChangePersonVersion(ClickEvent<Button> event) {
        var person = service.findPersonWithVersion();
        person.setName(randomText());
        service.merge(person);
    }

    private void onMergePersonVersion(ClickEvent<Button> event) {
        try {
        personWithVersionEntity.setName(randomText());
        service.merge(personWithVersionEntity);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private void clean() {
        personLabel.setText("");
    }

    private String randomText() {
        return LocalTime.now().toString();
    }

}
