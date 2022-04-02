package com.example.application.views.security;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route(value = "bean")
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class SecurityMainView extends VerticalLayout {

    public SecurityMainView(@Autowired MessageBean bean) {
        Button button = new Button("Click me",
                e -> Notification.show(bean.getMessage()));
        add(button);
    }

}