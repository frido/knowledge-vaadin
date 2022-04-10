package com.example.application.views.security;

import javax.servlet.http.HttpSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "bean")
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class SecurityMainView extends VerticalLayout {

    public SecurityMainView(@Autowired MessageBean bean, @Autowired MySessionBean sessionBean, @Autowired HttpSession session) {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        SecurityContext springSession = SecurityContextHolder.getContext();
        Button button = new Button("Click me",
                e -> Notification.show(bean.getMessage()));
        add(button);
    }

}