/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.ClassFilterBuilder;
import org.kasource.commons.reflection.MethodFilterBuilder;
import org.kasource.commons.util.reflection.AnnotationsUtils;
import org.kasource.commons.util.reflection.InterfaceUtils;
import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.annotations.listener.EventListenerFilter;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * @author rikardwigforss
 * 
 */
public abstract class AbstractEventListenerRegister implements EventListenerRegister {
    protected static final Collection<EventListenerRegistration> EMPTY_LISTENER_COLLECTION = new HashSet<EventListenerRegistration>();

    private EventRegister eventRegister;
    private BeanResolver beanResolver;

    /**
     * Constructor.
     * 
     * @param eventRegister
     *            Event Register.
     * @param beanResolver
     *            Bean Resolver.
     **/
    public AbstractEventListenerRegister(EventRegister eventRegister, BeanResolver beanResolver) {
        this.eventRegister = eventRegister;
        this.beanResolver = beanResolver;
    }

    /**
     * Filter interfaces.
     * 
     * @param interfaces
     *            Interfaces to filter.
     */
    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {
    }

    /**
     * Returns all lister interfaces form the listener object that is a Ka-Event registered event.
     * 
     * @param listener
     *            Listener object to inspect.
     * 
     * @return All interfaces from listener object that is registered with Ka-Event.
     **/
    @SuppressWarnings("unchecked")
    private Set<Class<? extends EventListener>> getRegisteredInterfaces(EventListener listener) {
        Set<Class<?>> interfaces = InterfaceUtils.getInterfaces(listener.getClass(), new ClassFilterBuilder().extendsType(EventListener.class).build());
        Set<Class<? extends EventListener>> registeredEvents = new HashSet<Class<? extends EventListener>>();
        for (Class<?> interfaceClass : interfaces) {
            if (eventRegister.hasEventByInterface((Class<? extends EventListener>) interfaceClass)) {
                registeredEvents.add((Class<? extends EventListener>) interfaceClass);
            }
        }

        filterInterfaces(registeredEvents);
        return registeredEvents;
    }

    /**
     * Register listener to listening on sourceObject.
     * 
     * @param listener
     *            Listener.
     * @param eventClass
     *            Event Class.
     * @param sourceObject
     *            Source object to listen to.
     * @param filters
     *            Event Filters.
     **/
    protected abstract void addListener(Object listener, Class<? extends EventObject> eventClass, Object sourceObject,
                List<EventFilter<? extends EventObject>> filters);

    /**
     * Register listener to listening on sourceObject.
     * 
     * @param listener
     *            Listener.
     * @param sourceObject
     *            Source object to listen to.
     **/
    protected void register(Object listener, Object sourceObject) {
        register(listener, sourceObject, null);

    }

    /**
     * Register a listener to a source object using filters.
     * 
     * @param listener
     *            Listener to register.
     * @param sourceObject
     *            Source object to listen to.
     * @param filters
     *            List of filters.
     **/
    protected void register(Object listener, Object sourceObject, List<EventFilter<? extends EventObject>> filters) {
        boolean listenerAdded = false;
        if (filters == null) {
            filters = new ArrayList<EventFilter<? extends EventObject>>();
        }
        listenerAdded = registerEventListenerByInterface(listener, sourceObject, filters);
        listenerAdded |= registerEventListenerByAnnotation(listener, sourceObject, filters);        
       
        if (!listenerAdded) {
            throw new IllegalStateException(listener
             + " does not implement any registered Event listener interfaces or any registered Event annotations!");
        }

    }

    private boolean registerEventListenerByAnnotation(Object listener, Object sourceObject,
                List<EventFilter<? extends EventObject>> filters) {
        boolean listenerAdded = false;
        Set<Class<? extends Annotation>> registeredAnnotations = eventRegister.getRegisteredEventAnnotations();
        
        Map<Class<? extends Annotation>, Set<Method>>  methodAnnotations = AnnotationsUtils.findAnnotatedMethods(listener.getClass(), registeredAnnotations);
        for (Map.Entry<Class<? extends Annotation>, Set<Method>> annotationEntry : methodAnnotations.entrySet()){
            Set<Method> methods = annotationEntry.getValue();
            Class<? extends Annotation> annotation = annotationEntry.getKey();
            if(methods.size() > 1) {
                throw new IllegalStateException("More than one method found annotated with " + annotation + " in class " + listener.getClass());
            }            
            addAnnotatedListenerMethod(listener, sourceObject, annotation, methods.iterator().next(), filters);
            listenerAdded = true;
        }
        return listenerAdded;
    }

    private boolean registerEventListenerByInterface(Object listener, Object sourceObject,
                List<EventFilter<? extends EventObject>> filters) {
        boolean listenerAdded = false;
        if (listener instanceof EventListener) {
            // Add one lister registration per registered interface of the listener
            Set<Class<? extends EventListener>> interfaces = getRegisteredInterfaces(((EventListener) listener));
            if (!interfaces.isEmpty()) {
                for (Class<? extends EventListener> interfaceClass : interfaces) {
                    registerInterfaceListener(listener, sourceObject, filters, interfaceClass);
                    listenerAdded = true;
                }
            }
        }
        return listenerAdded;
    }

    private void registerInterfaceListener(Object listener, Object sourceObject,
                List<EventFilter<? extends EventObject>> filters, Class<? extends EventListener> interfaceClass) {
        List<EventFilter<? extends EventObject>> regFilters = new ArrayList<EventFilter<? extends EventObject>>();
        regFilters.addAll(filters);
        Set<Method> methods = MethodUtils.getDeclaredMethods(interfaceClass, new MethodFilterBuilder().returnType(Void.TYPE).numberOfParameters(1).build());
        // Only inspect method level @EventListerFilter on single method interfaces
        if(methods.size() == 1) {
            addEventFilterByAnnotation(listener, methods.iterator().next(), filters);
        } else {
            addEventFilterByAnnotation(listener, null, filters);
            // If any method is annotated with @EventListenerFilter an
            // exception could be thrown here.
        }
        addListener(listener, eventRegister.getEventByInterface(interfaceClass).getEventClass(),
                    sourceObject, filters);
    }

    private void addAnnotatedListenerMethod(Object listener, Object sourceObject, Class<? extends Annotation> annotation, Method method, List<EventFilter<? extends EventObject>> filters){
        List<EventFilter<? extends EventObject>> regFilters = new ArrayList<EventFilter<? extends EventObject>>();
        regFilters.addAll(filters);
        addEventFilterByAnnotation(listener, method, regFilters);
        validateAnnotatedEventMethod(listener, method, annotation, eventRegister.getEventByAnnotation(annotation).getEventClass());
        addListener(listener, eventRegister.getEventByAnnotation(annotation)
                    .getEventClass(), sourceObject, regFilters);
       
    }
    
    
    /**
     * Validate that the method is valid for event invocation, that is has only parameter that
     * can be assigned from the eventClass. Throws exception of validation fails.
     * 
     * @param listener      Listener object which has the method
     * @param method        Method annotated with annotation
     * @param annotation    Annotation method is annotated with.
     * @param eventClass    Event Class associated with the annotation.
     * @throws IllegalStateException if the annotated is not valid for event invocation.
     **/
    private void validateAnnotatedEventMethod(Object listener, 
                                              Method method, 
                                              Class<? extends Annotation> annotation, 
                                              Class<? extends EventObject> eventClass) throws IllegalStateException {
        if (method.getParameterTypes().length == 0) {
            throw new IllegalStateException(listener + "." + method.getName() + " is annotated with @"
                        + annotation.getSimpleName()
                        + " but the method has no paremeters, should have one paramter of type " + eventClass);
        } else if (method.getParameterTypes().length > 1) {
            throw new IllegalStateException(listener + "." + method.getName() + " is annotated with @"
                        + annotation.getSimpleName()
                        + " but the method has more than one paremeter, should have one paramter of type " + eventClass);   
        }
        
        if (!method.getParameterTypes()[0].isAssignableFrom(eventClass)) {
        throw new IllegalStateException(listener + "." + method.getName() + " is annotated with @"
                    + annotation.getSimpleName()
                    + " but the method parameter could no be assigned from "
                    + eventClass);
        }
    }
    
    /**
     * Returns a list of EventFilters based on EventListenerFilter Annotation.
     * 
     * @param listener
     *            Listener to inspect.
     * @param filters
     *            List of filters to add found filters to.
     **/
    @SuppressWarnings("unchecked")
    private void addEventFilterByAnnotation(Object listener, Method method, List<EventFilter<? extends EventObject>> filters) {
        EventListenerFilter classfilter = listener.getClass().getAnnotation(EventListenerFilter.class);
        EventListenerFilter methodFilter = null;
        if(method != null) {
            methodFilter = method.getAnnotation(EventListenerFilter.class);
        }
        // Method annotations overrides any class level annotations.
        if(methodFilter != null) {
            for (String beanName : methodFilter.value()) {
                EventFilter<EventObject> filter = beanResolver.getBean(beanName, EventFilter.class);
                filters.add(filter);
            }
        } else if (classfilter != null){
            for (String beanName : classfilter.value()) {
                EventFilter<EventObject> filter = beanResolver.getBean(beanName, EventFilter.class);
                filters.add(filter);
            }
        }
        

    }

    /**
     * Returns a list of filters that is applicable for the given eventClass.
     * 
     * @param eventClass
     *            Event class
     * @param filters
     *            Filter candidates.
     * 
     * @return List of filters that can be applied to the eventClass.
     **/
    @SuppressWarnings("unchecked")
    protected List<EventFilter<? extends EventObject>> getApplicableFilters(Class<? extends EventObject> eventClass,
                List<EventFilter<? extends EventObject>> filters) {
        List<EventFilter<? extends EventObject>> applicableFilters = new ArrayList<EventFilter<? extends EventObject>>();
        for (EventFilter<? extends EventObject> filter : filters) {

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
