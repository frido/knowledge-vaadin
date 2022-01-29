package com.example.application.knowledge;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.model.domain.NavigableRole;
import org.hibernate.stat.internal.StatisticsImpl;

public class CustomStatisticsImpl extends StatisticsImpl {

    private transient MessageQueue queue = MessageQueue.getInstance();

    public CustomStatisticsImpl(SessionFactoryImplementor sessionFactory) {
        super(sessionFactory);        
    }

    private void add(String method, String hql, int rows, long time) {
        add(method, hql + ": " + rows + " : " + time);
    }

    private void add(String method, NavigableRole msg, String detail) {
        add(method, msg.getNavigableName() + ": " + detail);
    }

    private void add(String method, String msg, String detail) {
        add(method, msg + ": " + detail);
    }

    private void add(String method, String msg) {
        queue.add("CustomStatisticsImpl", method, msg);
    }

    private void add(String method) {
        queue.add("CustomStatisticsImpl", method, null);
    }

    // private void add(String msg) {
    //     queue.add(msg);
    // }

    @Override
    public void updateCollection(String role) {
        add("updateCollection", role);
        super.updateCollection(role);
    }

    @Override
    public void updateEntity(String entityName) {
        add("updateEntity", entityName);
        super.updateEntity(entityName);
    }

    @Override
    public void openSession() {
        add("openSession");
        super.openSession();
    }

    @Override
    public void closeSession() {
        add("closeSession");
        super.closeSession();
    }

    @Override
    public void flush() {
        add("flush");
        super.flush();
    }

    @Override
    public void connect() {
        add("connect");
        super.connect();
    }

    @Override
    public void prepareStatement() {
        add("prepareStatement");
        super.prepareStatement();
    }

    @Override
    public void closeStatement() {
        add("closeStatement");
        super.closeStatement();
    }

    @Override
    public void endTransaction(boolean success) {
        add("endTransaction", String.valueOf(success));
        super.endTransaction(success);
    }

    @Override
    public void loadEntity(String entityName) {
        add("loadEntity", entityName);
        super.loadEntity(entityName);
    }

    @Override
    public void fetchEntity(String entityName) {
        add("fetchEntity", entityName);
        super.fetchEntity(entityName);
    }

    @Override
    public void insertEntity(String entityName) {
        add("insertEntity", entityName);
        super.insertEntity(entityName);
    }

    @Override
    public void deleteEntity(String entityName) {
        add("deleteEntity", entityName);
        super.deleteEntity(entityName);
    }

    @Override
    public void optimisticFailure(String entityName) {
        add("optimisticFailure", entityName);
        super.optimisticFailure(entityName);
    }

    @Override
    public void loadCollection(String role) {
        add("loadCollection", role);
        super.loadCollection(role);
    }

    @Override
    public void fetchCollection(String role) {
        add("fetchCollection", role);
        super.fetchCollection(role);
    }

    @Override
    public void recreateCollection(String role) {
        add("recreateCollection", role);
        super.recreateCollection(role);
    }

    @Override
    public void removeCollection(String role) {
        add("removeCollection", role);
        super.removeCollection(role);
    }

    @Override
    public void entityCachePut(NavigableRole entityName, String regionName) {
        add("entityCachePut", entityName, regionName);
        super.entityCachePut(entityName, regionName);
    }

    @Override
    public void entityCacheHit(NavigableRole entityName, String regionName) {
        add("entityCacheHit", entityName, regionName);
        super.entityCacheHit(entityName, regionName);
    }

    @Override
    public void entityCacheMiss(NavigableRole entityName, String regionName) {
        add("entityCacheMiss", entityName, regionName);
        super.entityCacheMiss(entityName, regionName);
    }

    @Override
    public void naturalIdCachePut(NavigableRole rootEntityName, String regionName) {
        add("naturalIdCachePut", rootEntityName, regionName);
        super.naturalIdCachePut(rootEntityName, regionName);
    }

    @Override
    public void naturalIdCacheHit(NavigableRole rootEntityName, String regionName) {
        add("naturalIdCacheHit", rootEntityName, regionName);
        super.naturalIdCacheHit(rootEntityName, regionName);
    }

    @Override
    public void naturalIdCacheMiss(NavigableRole rootEntityName, String regionName) {
        add("naturalIdCacheMiss", rootEntityName, regionName);
        super.naturalIdCacheMiss(rootEntityName, regionName);
    }

    @Override
    public void naturalIdQueryExecuted(String rootEntityName, long time) {
        add("naturalIdQueryExecuted", rootEntityName + ": " + String.valueOf(time));
        super.naturalIdQueryExecuted(rootEntityName, time);
    }

    @Override
    public void collectionCachePut(NavigableRole collectionRole, String regionName) {
        add("collectionCachePut", collectionRole, regionName);
        super.collectionCachePut(collectionRole, regionName);
    }

    @Override
    public void collectionCacheHit(NavigableRole collectionRole, String regionName) {
        add("collectionCacheHit", collectionRole, regionName);
        super.collectionCacheHit(collectionRole, regionName);
    }

    @Override
    public void collectionCacheMiss(NavigableRole collectionRole, String regionName) {
        add("collectionCacheMiss", collectionRole, regionName);
        super.collectionCacheMiss(collectionRole, regionName);
    }

    @Override
    public void queryExecuted(String hql, int rows, long time) {
        add("queryExecuted", hql, rows, time);
        super.queryExecuted(hql, rows, time);
    }

    @Override
    public void queryCacheHit(String hql, String regionName) {
        add("queryCacheHit", hql, regionName);
        super.queryCacheHit(hql, regionName);
    }

    @Override
    public void queryCacheMiss(String hql, String regionName) {
        add("queryCacheMiss", hql, regionName);
        super.queryCacheMiss(hql, regionName);
    }

    @Override
    public void queryCachePut(String hql, String regionName) {
        add("queryCachePut", hql, regionName);
        super.queryCachePut(hql, regionName);
    }
}
