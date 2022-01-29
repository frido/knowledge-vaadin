package com.example.application.knowledge;

import java.util.Map;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.AutoFlushEvent;
import org.hibernate.event.spi.AutoFlushEventListener;
import org.hibernate.event.spi.ClearEvent;
import org.hibernate.event.spi.ClearEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.hibernate.event.spi.DirtyCheckEvent;
import org.hibernate.event.spi.DirtyCheckEventListener;
import org.hibernate.event.spi.EvictEvent;
import org.hibernate.event.spi.EvictEventListener;
import org.hibernate.event.spi.FlushEntityEvent;
import org.hibernate.event.spi.FlushEntityEventListener;
import org.hibernate.event.spi.FlushEvent;
import org.hibernate.event.spi.FlushEventListener;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;
import org.hibernate.event.spi.LockEvent;
import org.hibernate.event.spi.LockEventListener;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;
import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreCollectionRecreateEvent;
import org.hibernate.event.spi.PreCollectionRecreateEventListener;
import org.hibernate.event.spi.PreCollectionRemoveEvent;
import org.hibernate.event.spi.PreCollectionRemoveEventListener;
import org.hibernate.event.spi.PreCollectionUpdateEvent;
import org.hibernate.event.spi.PreCollectionUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.RefreshEvent;
import org.hibernate.event.spi.RefreshEventListener;
import org.hibernate.event.spi.ReplicateEvent;
import org.hibernate.event.spi.ReplicateEventListener;
import org.hibernate.event.spi.ResolveNaturalIdEvent;
import org.hibernate.event.spi.ResolveNaturalIdEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

public class CustomMergeEventListener implements MergeEventListener ,
ClearEventListener,
AutoFlushEventListener,
DeleteEventListener,
DirtyCheckEventListener,
LoadEventListener,
ResolveNaturalIdEventListener,
InitializeCollectionEventListener,
SaveOrUpdateEventListener,
PersistEventListener,
ReplicateEventListener,
FlushEventListener,
FlushEntityEventListener,
EvictEventListener,
LockEventListener,
RefreshEventListener,
PreLoadEventListener,
PreDeleteEventListener,
PreUpdateEventListener,
PreInsertEventListener,
PostLoadEventListener,
PostDeleteEventListener,
PostUpdateEventListener,
PostInsertEventListener,
PreCollectionRecreateEventListener,
PreCollectionRemoveEventListener,
PreCollectionUpdateEventListener,
PostCollectionRecreateEventListener,
PostCollectionRemoveEventListener,
PostCollectionUpdateEventListener
{

    private transient MessageQueue queue = MessageQueue.getInstance(); 

    private void add(String method, Object event) {
        queue.add("CustomMergeEventListener", method, String.valueOf(event));
    }

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        add("onMerge1", event);
    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        add("onMerge1", event);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        add("requiresPostCommitHanding", persister);
        return false;
    }

    @Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        add("onPostUpdateCollection", event);
        
    }

    @Override
    public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        add("onPostRemoveCollection", event);
        
    }

    @Override
    public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        add("onPostRecreateCollection", event);
        
    }

    @Override
    public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        add("onPreUpdateCollection", event);
        
    }

    @Override
    public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        add("onPreRemoveCollection", event);
        
    }

    @Override
    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        add("onPreRecreateCollection", event);
        
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        add("onPostInsert", event);
        
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        add("onPostUpdate", event);
        
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        add("onPostDelete", event);
        
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        add("onPostLoad", event);
        
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        add("onPreInsert", event);
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        add("onPreUpdate", event);
        return false;
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        add("onPreDelete", event);
        return false;
    }

    @Override
    public void onPreLoad(PreLoadEvent event) {
        add("onPreLoad", event);
        
    }

    @Override
    public void onRefresh(RefreshEvent event) throws HibernateException {
        add("onRefresh", event);
        
    }

    @Override
    public void onRefresh(RefreshEvent event, Map refreshedAlready) throws HibernateException {
        add("onRefresh", event);
        
    }

    @Override
    public void onLock(LockEvent event) throws HibernateException {
        add("onLock", event);
        
    }

    @Override
    public void onEvict(EvictEvent event) throws HibernateException {
        add("onEvict", event);
        
    }

    @Override
    public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
        add("onFlushEntity", event);
        
    }

    @Override
    public void onFlush(FlushEvent event) throws HibernateException {
        EventHolder eh = new EventHolder();
        eh.add("getNumberOfCollectionsProcessed", event.getNumberOfCollectionsProcessed());
        eh.add("getNumberOfEntitiesProcessed", event.getNumberOfEntitiesProcessed());
        add("onFlush", eh);
        
    }

    @Override
    public void onReplicate(ReplicateEvent event) throws HibernateException {
        add("onReplicate", event);
        
    }

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        add("onPersist", event);
        
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
        add("onPersist", event);
        
    }

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        add("onSaveOrUpdate", event);
        
    }

    @Override
    public void onInitializeCollection(InitializeCollectionEvent event) throws HibernateException {
        add("onInitializeCollection", event);
        
    }

    @Override
    public void onResolveNaturalId(ResolveNaturalIdEvent event) throws HibernateException {
        add("onResolveNaturalId", event);
        
    }

    @Override
    public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
        add("onLoad", event);
        
    }

    @Override
    public void onDirtyCheck(DirtyCheckEvent event) throws HibernateException {
        add("onDirtyCheck", event);
        
    }

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        add("onDelete", event);
        
    }

    @Override
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        add("onDelete", event);
        
    }

    @Override
    public void onAutoFlush(AutoFlushEvent event) throws HibernateException {
        add("onAutoFlush", event);
        
    }

    @Override
    public void onClear(ClearEvent event) {
        add("onClear", event);
        
    }
    
}
