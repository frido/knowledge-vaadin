package com.example.application.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

@Component
public class CustomInterceptorImpl extends EmptyInterceptor {

    static long serialVersionUID = 1l;

    private transient MessageQueue queue = MessageQueue.getInstance();

    private void add(String method, Object payload) {
        queue.add("CustomInterceptorImpl", method, String.valueOf(payload));
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        add("afterTransactionBegin", tx.getStatus());
        super.afterTransactionBegin(tx);
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        add("beforeTransactionCompletion", tx.getStatus());
        super.beforeTransactionCompletion(tx);
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        add("afterTransactionCompletion", tx.getStatus());
        super.afterTransactionCompletion(tx);
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        add("findDirty", entity);
        return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public Object getEntity(String entityName, Serializable id) {
        add("getEntity", entityName);
        return super.getEntity(entityName, id);
    }

    @Override
    public String getEntityName(Object object) {
        add("getEntityName", object);
        return super.getEntityName(object);
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
        add("instantiate", entityName + ", " + entityMode);
        return super.instantiate(entityName, entityMode, id);
    }

    @Override
    public Boolean isTransient(Object entity) {
        add("isTransient", entity);
        return super.isTransient(entity);
    }

    @Override
    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
        add("onCollectionRecreate", collection);
        super.onCollectionRecreate(collection, key);
    }

    @Override
    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
        add("onCollectionRemove", collection);
        super.onCollectionRemove(collection, key);
    }

    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        add("onCollectionUpdate", collection);
        super.onCollectionUpdate(collection, key);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        add("onDelete", entity);
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        add("onFlushDirty", entity);
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        add("onLoad", entity);
        return super.onLoad(entity, id, state, propertyNames, types);
    }    

    @Override
    public String onPrepareStatement(String sql) {
        add("onPrepareStatement", sql);
        return super.onPrepareStatement(sql);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        add("onSave", entity);
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void postFlush(Iterator entities) {
        add("postFlush", toStream(entities));
        super.postFlush(entities);
    }

    @Override
    public void preFlush(Iterator entities) {
        add("preFlush", toStream(entities));
        super.preFlush(entities);
    }

    private String toStream(Iterator entities) {
        List<String> list = new ArrayList<>();
        while(entities.hasNext()) {
            list.add(String.valueOf(entities.next()));
        }
        return String.join(",", list);
    }
    
}
