package com.example.application.knowledge;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class CustomEventListenerIntegrator implements Integrator  {

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
                final EventListenerRegistry eventListenerRegistry =
                serviceRegistry.getService(EventListenerRegistry.class);

                eventListenerRegistry.appendListeners(EventType.AUTO_FLUSH, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.CLEAR, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.DELETE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.DIRTY_CHECK, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.EVICT, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.FLUSH, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.FLUSH_ENTITY, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.INIT_COLLECTION, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.LOAD, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.LOCK, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.MERGE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PERSIST, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PERSIST_ONFLUSH, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_RECREATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_REMOVE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COMMIT_DELETE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COMMIT_INSERT, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_COMMIT_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_DELETE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_INSERT, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_LOAD, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.POST_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_COLLECTION_RECREATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_COLLECTION_REMOVE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_COLLECTION_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_INSERT, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_LOAD, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.REFRESH, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.REPLICATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.RESOLVE_NATURAL_ID, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.SAVE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.SAVE_UPDATE, new CustomMergeEventListener());
                eventListenerRegistry.appendListeners(EventType.UPDATE, new CustomMergeEventListener());
        
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        
    }
    
}
