/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.annotations.listener.EventListenerFilter;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.event.register.NoSuchEventException;

/**
 * @author rikardwigforss
 * 
 */
public abstract class AbstractEventListenerRegister implements EventListenerRegister {
    protected static final Collection<EventListenerRegistration> 
    	EMPTY_LISTENER_COLLECTION = new HashSet<EventListenerRegistration>();

    protected EventRegister eventRegister;
    protected BeanResolver beanResolver;
    
    
    
    public AbstractEventListenerRegister(EventRegister eventRegister, BeanResolver beanResolver) {
        this.eventRegister = eventRegister;
        this.beanResolver = beanResolver;
    }

    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends EventListener>> getRegisteredInterfaces(EventListener listener) {
        Set<Class<?>> interfaces = ReflectionUtils.getInterfacesExtending(listener, EventListener.class);
        Set<Class<? extends EventListener>> registeredEvents = new HashSet<Class<? extends EventListener>>();
        for (Class<?> interfaceClass : interfaces) {
            try {
            	eventRegister.getEventByInterface((Class<? extends EventListener>) interfaceClass);
            	registeredEvents.add((Class<? extends EventListener>) interfaceClass);
            } catch (NoSuchEventException nse) { }
        }
            
        filterInterfaces(registeredEvents);
        return registeredEvents;
    }

    protected abstract void addListener(EventListener listener, Class<? extends EventObject> eventClass,
            Object sourceObject, List<EventFilter<EventObject>> filters);

    protected void register(EventListener listener, Object sourceObject) {
        register(listener, sourceObject, null);

    }

    @SuppressWarnings("unchecked")
    protected void register(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        // find filters by annotation
        EventListenerFilter filterAnnotation = listener.getClass().getAnnotation(EventListenerFilter.class);
        if (filterAnnotation != null && filterAnnotation.value().length > 0) {
            if (filters == null) {
                filters = new ArrayList<EventFilter<EventObject>>();
            }
            for (String beanName : filterAnnotation.value()) {
           
                EventFilter<EventObject> filter =  beanResolver.getBean(beanName, EventFilter.class);
                filters.add(filter);
            }
        }
        
        // Add one lister registration per registered interface of the listener
        Set<Class<? extends EventListener>> interfaces = getRegisteredInterfaces(listener);
        if (interfaces.isEmpty()) {
        	throw new IllegalStateException(listener + " does not implement any registered event listener!");
        } else {
        	for (Class<? extends EventListener> interfaceClass : interfaces) {
        		addListener(listener, 
        		            eventRegister.getEventByInterface(interfaceClass).getEventClass(), 
        		            sourceObject,
        				    filters);
        	}
        }

    }

   

    @SuppressWarnings("unchecked")
    protected List<EventFilter<EventObject>> getApplicableFilters(Class<? extends EventObject> eventClass,
            List<EventFilter<EventObject>> filters) {
        List<EventFilter<EventObject>> applicableFilters = new ArrayList<EventFilter<EventObject>>();
        for (EventFilter<EventObject> filter : filters) {

            Class<? extends EventObject> filterEventClass = (Class<? extends EventObject>) ((ParameterizedType) filter
                    .getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];

            if (filterEventClass.isAssignableFrom(eventClass)) {
                applicableFilters.add(filter);
            }

        }

        return applicableFilters;
    }
}
