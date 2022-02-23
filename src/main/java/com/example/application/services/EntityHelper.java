package com.example.application.services;

import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;

public class EntityHelper {

    private EntityHelper() {}

    public static String managetEntities(EntityManager em) {
        SessionImplementor session = em.unwrap( SessionImplementor.class );
        String msg = "";
        for(Map.Entry<Object,EntityEntry> x : session.getPersistenceContext().reentrantSafeEntityEntries()) {
            msg = msg + ", " + String.valueOf(x.getKey());
        }
        return msg;
    } 
    
}
