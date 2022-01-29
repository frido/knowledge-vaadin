package com.example.application.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.example.application.knowledge.Person;

import org.springframework.stereotype.Service;

@Service
public class TransactionalService {
    @PersistenceContext
    EntityManager em;

    @Transactional(value = TxType.REQUIRED) 
    public String getTeamNameRequired(Person person) {
        return person.getTeam().getName();
    }

    @Transactional(value = TxType.REQUIRES_NEW) 
    public String getTeamNameRequiresNew(Person person) {
        return person.getTeam().getName();
    }
}
