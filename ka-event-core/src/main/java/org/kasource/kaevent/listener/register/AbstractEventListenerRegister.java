/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.FileTypeMap;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * @author rikardwigforss
 *
 */
public abstract class AbstractEventListenerRegister implements EventListenerRegister{
    protected static final Collection<EventListenerRegistration> EMPTY_LISTENER_COLLECTION = new HashSet<EventListenerRegistration>();
    
    protected EventRegister eventRegister;
    
    public AbstractEventListenerRegister(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }
    
    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {}
    
    
    @SuppressWarnings("unchecked")
    private Set<Class<? extends EventListener>> getRegisteredInterfaces(EventListener listener) {
        Set<Class<?>> interfaces = ReflectionUtils.getInterfacesExtending(listener, EventListener.class);
        Set<Class<? extends EventListener>> registeredEvents = new HashSet<Class<? extends EventListener>>();
        for(Class<?> interfaceClass : interfaces) {
            if(eventRegister.getEventByInterface((Class<? extends EventListener>) interfaceClass) != null) {
                registeredEvents.add((Class<? extends EventListener>) interfaceClass);
            }
        }
        filterInterfaces(registeredEvents);
        return registeredEvents;
    }
     
    protected abstract void addListener(EventListener listener, Class<? extends EventObject> eventClass, Object sourceObject,List<EventFilter<EventObject>> filters);
    
  
    protected void register(EventListener listener, Object sourceObject) {
        register(listener, sourceObject, null);
       
    }
    
    
    protected void register(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        Set<Class<? extends EventListener>> interfaces = getRegisteredInterfaces(listener);
        for(Class<? extends EventListener> interfaceClass : interfaces) {
            addListener(listener, eventRegister.getEventByInterface(interfaceClass).getEventClass(), sourceObject, filters);
        }
        
    }

    @SuppressWarnings("unchecked")
    protected Map<Class<? extends EventObject> ,List<EventFilter<EventObject>>> getApplicableFilters(EventListener listener,
            List<EventFilter<EventObject>> filters) {
        Map<Class<? extends EventObject>, List<EventFilter<EventObject>>> applicableFilters = new HashMap<Class<? extends EventObject>, List<EventFilter<EventObject>>>();
        for (EventFilter<EventObject> filter : filters) {
            
            Class<? extends EventObject> eventClass = (Class<? extends EventObject>) ((ParameterizedType) filter
                    .getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            
            Class<?>[] interfaces = listener.getClass().getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                if (EventListener.class.isAssignableFrom(interfaceClass)) {
                    
                    EventConfig eventConfig = eventRegister
                            .getEventByInterface((Class<? extends EventListener>) interfaceClass);
                    
                    if (eventConfig != null) {
                        if (eventClass.isAssignableFrom(eventConfig.getEventClass())) {
                            List<EventFilter<EventObject>> filterList = applicableFilters.get(eventConfig.getEventClass());
                            if(filterList == null) {
                                filterList = new ArrayList<EventFilter<EventObject>>();
                                applicableFilters.put(eventConfig.getEventClass(), filterList);
                            }
                            filterList.add(filter);
                        }
                    }

                }
            }
        }
        return applicableFilters;
    }
}
