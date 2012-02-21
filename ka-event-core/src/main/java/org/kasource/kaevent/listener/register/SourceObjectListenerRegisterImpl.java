package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * Register of all source object listeners.
 * 
 * @author rikardwi
 **/
public class SourceObjectListenerRegisterImpl extends AbstractEventListenerRegister implements
            SourceObjectListenerRegister {

    private Map<Class<? extends EventObject>, 
                Map<Object, 
                    Map<Object, 
                    EventListenerRegistration>
                >
            > objectListenersByEvent = 
                new HashMap<Class<? extends EventObject>, Map<Object, Map<Object, EventListenerRegistration>>>();

    /**
     * Constructor.
     * 
     * @param eventRegister Event Register
     * @param beanResolver  Bean Resolver
     **/
    public SourceObjectListenerRegisterImpl(EventRegister eventRegister, BeanResolver beanResolver) {
        super(eventRegister, beanResolver);
    }

    @Override
    public Collection<EventListenerRegistration> getListenersByEvent(EventObject event) {
        Map<Object, Map<Object, EventListenerRegistration>> objectListenerMap = objectListenersByEvent.get(event
                    .getClass());
        if (objectListenerMap != null) {
            Map<Object, EventListenerRegistration> listenerByObjectMap = objectListenerMap
                        .get(event.getSource());
            if (listenerByObjectMap != null) {
                return listenerByObjectMap.values();
            }
        }
        return EMPTY_LISTENER_COLLECTION;
    }

    @Override
    protected void addListener(Object listener, Class<? extends EventObject> eventClass, Object sourceObject,
                List<EventFilter<EventObject>> filters) {
        Map<Object, Map<Object, EventListenerRegistration>> objectListenerMap = objectListenersByEvent
                    .get(eventClass);
        if (objectListenerMap == null) {
            objectListenerMap = new WeakHashMap<Object, Map<Object, EventListenerRegistration>>();
            objectListenersByEvent.put(eventClass, objectListenerMap);
        }
        Map<Object, EventListenerRegistration> listenerByObjectMap = objectListenerMap.get(sourceObject);
        if (listenerByObjectMap == null) {
            listenerByObjectMap = new WeakHashMap<Object, EventListenerRegistration>();
            objectListenerMap.put(sourceObject, listenerByObjectMap);
        }

        if (filters != null) {
            filters = getApplicableFilters(eventClass, filters);
        }
        listenerByObjectMap.put(listener, new EventListenerRegistration(listener, filters));
    }

    @Override
    public void unregisterListener(Object listener, Object sourceObject) {

        for (Map<Object, Map<Object, EventListenerRegistration>> objectListenerMap : objectListenersByEvent
                    .values()) {
            Map<Object, EventListenerRegistration> listenerByObjectMap = objectListenerMap.get(sourceObject);
            if (listenerByObjectMap != null) {
                listenerByObjectMap.remove(listener);
            }
        }

    }

    @Override
    public void registerListener(Object listener, Object sourceObject) {
        super.register(listener, sourceObject);

    }

    @Override
    public void registerListener(Object listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        super.register(listener, sourceObject, filters);

    }

}
