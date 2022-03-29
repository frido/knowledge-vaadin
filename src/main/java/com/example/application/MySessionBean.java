package com.example.application;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MySessionBean implements InitializingBean  {
    
    public MySessionBean() {
        System.out.println();
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
        
    }
}
