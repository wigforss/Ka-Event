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

    private EventRegister eventRegister;
    private BeanResolver beanResolver;
    
    
    /**
     * Constructor.
     * 
     * @param eventRegister Event Register.
     * @param beanResolver  Bean Resolver.
     **/
    public AbstractEventListenerRegister(EventRegister eventRegister, BeanResolver beanResolver) {
        this.eventRegister = eventRegister;
        this.beanResolver = beanResolver;
    }

    /**
     * Filter interfaces.
     * 
     * @param interfaces Interfaces to filter.
     */
    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {
    }

    /**
     * Return registered interfaces.
     * 
     * @param listener  Listener.
     * 
     * @return 
     **/
    @SuppressWarnings("unchecked")
    private Set<Class<? extends EventListener>> getRegisteredInterfaces(EventListener listener) {
        Set<Class<?>> interfaces = ReflectionUtils.getInterfacesExtending(listener, EventListener.class);
        Set<Class<? extends EventListener>> registeredEvents = new HashSet<Class<? extends EventListener>>();
        for (Class<?> interfaceClass : interfaces) {
            try {
            	eventRegister.getEventByInterface((Class<? extends EventListener>) interfaceClass);
            	registeredEvents.add((Class<? extends EventListener>) interfaceClass);
            } catch (NoSuchEventException nse) { 
                
            }
        }
            
        filterInterfaces(registeredEvents);
        return registeredEvents;
    }

    
    /**
     * Register listener to listening on sourceObject.
     * 
     * @param listener      Listener.
     * @param eventClass    Event Class.
     * @param sourceObject  Source object to listen to.
     * @param filters       Event Filters.
     **/
    protected abstract void addListener(EventListener listener, Class<? extends EventObject> eventClass,
            Object sourceObject, List<EventFilter<EventObject>> filters);

    /**
     * Register listener to listening on sourceObject.
     * 
     * @param listener      Listener.
     * @param sourceObject  Source object to listen to.
     **/
    protected void register(EventListener listener, Object sourceObject) {
        register(listener, sourceObject, null);

    }

   /**
    * Register a listener to a source object using filters.
    * 
    * @param listener       Listener to register.
    * @param sourceObject   Source object to listen to.
    * @param filters        List of filters.
    **/
    protected void register(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        if (filters == null) {
            filters = new ArrayList<EventFilter<EventObject>>();
        }
        // find filters by annotation
        addEventFilterByAnnotation(listener, filters);
        
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

    /**
     * Returns a list of EventFilters based on EventListenerFilterAnnotation.
     * 
     * @param listener  Listener to inspect.
     * @param filters   List of filters to add found filters to.
     **/
    @SuppressWarnings("unchecked")
    private void addEventFilterByAnnotation(EventListener listener,
                List<EventFilter<EventObject>> filters) {
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
       
    }

   
    /**
     * Returns a list of filters that is applicable for the given eventClass.
     * 
     * @param eventClass    Event class
     * @param filters       Filter candidates.
     * 
     * @return List of filters that can be applied to the eventClass.
     **/
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

    /**
     * @return the eventRegister
     */
    protected EventRegister getEventRegister() {
        return eventRegister;
    }
}
