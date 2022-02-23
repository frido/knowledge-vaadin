package com.example.application.services;

import java.util.ArrayList;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.example.application.knowledge.MessageQueue;
import com.example.application.knowledge.Person;
import com.example.application.knowledge.PersonWithVersion;
import com.example.application.views.knowledge.LogType;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntityNewService {
    @PersistenceContext
    EntityManager em;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reaOnlyTransaction2(PersonWithVersion person) {
        var action = "reaOnlyTransaction2";
        log(action, String.valueOf(em));
        SessionImplementor session = em.unwrap( SessionImplementor.class );
        String msg = "";
        for(Map.Entry<Object,EntityEntry> x : session.getPersistenceContext().reentrantSafeEntityEntries()) {
            msg = msg + ", " + String.valueOf(x.getKey());
        }
        log(action, msg);
        em.merge(person);
        em.flush();
    }

    private void log(String method, String payload) {
        MessageQueue.getInstance().add(LogType.APP, "View", method, payload);
    }
}
