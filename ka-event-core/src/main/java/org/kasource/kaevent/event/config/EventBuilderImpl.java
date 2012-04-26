package org.kasource.kaevent.event.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.commons.reflection.filter.methods.MethodFilterList;
import org.kasource.commons.reflection.filter.methods.ReturnTypeMethodFilter;
import org.kasource.commons.reflection.filter.methods.SignatureMethodFilter;
import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.method.AnnotatedMethodMethodResolver;
import org.kasource.kaevent.event.method.MethodResolver;

public class EventBuilderImpl implements EventBuilder {
    private AnnotationMethodResolverExtractor methodResolverExtractor;
    private Class<? extends EventObject> eventClass;
    private String name;
    private Class<? extends EventListener> interfaceClass;
    private Method eventMethod;
    @SuppressWarnings("rawtypes")
    private MethodResolver methodResolver;
    private Class<? extends Annotation> annotationClass;
    private String eventQueue;
    private EventQueueRegister eventQueueRegister;
    
    /**
     * Creates a new builder for an event. If no name is given the name used
     * will be the fully qualified name of the event class.
     * 
     * Note that if eventClass is not annotated with @Event an interface and / or an annotation 
     * needs to be bound in order to build the event configuration successfully.
     * 
     * @param eventClass    The Event Class to build the event for.
     **/
    public EventBuilderImpl(BeanResolver beanResolver, EventQueueRegister eventQueueRegister, Class<? extends EventObject> eventClass){
        this.eventClass = eventClass;
        this.methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
        this.eventQueueRegister = eventQueueRegister;
    }
    
    
   
    @Override
    public EventBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    /**
     * Bind the interface class to the event, inspecting the @Event annotation of the event class.
     * Either an event method or a method resolver may be found by inspecting the annotations on the eventClass
     * and or the interface class.
     * 
     * @param interfaceClass The listener interface to bind to the eventClass.
     * 
     * @return This builder to allow chaining.
     **/
 /*   public EventBuilder bindInterface(Class<? extends EventListener> interfaceClass) {
        if(this.interfaceClass != null){
            throw new InvalidEventConfigurationException("An Event can only be bound to an Interface once");
        }
        this.interfaceClass = interfaceClass;
        return this;
    }
   */
    
    @Override
    public EventBuilder bindInterface(Class<? extends EventListener> interfaceClass, Method eventMethod) throws InvalidEventConfigurationException {
        if(this.interfaceClass != null){
            throw new InvalidEventConfigurationException("An Event can only be bound to an Interface once");
        }
        if(isEventAnnotated() && eventClass.getAnnotation(Event.class).listener() != EventListener.class) {
            throw new InvalidEventConfigurationException("The Event " + eventClass + " has already a bound interface via its @Event annotation");
        }
        this.interfaceClass = interfaceClass;
        this.eventMethod = eventMethod;
        return this;
    }
    
  
    @Override
    public EventBuilder bindInterface(Class<? extends EventListener> interfaceClass, @SuppressWarnings("rawtypes") MethodResolver methodResolver) throws InvalidEventConfigurationException {
        if(this.interfaceClass != null){
            throw new InvalidEventConfigurationException("An Event can only be bound to an Interface once");
        }
        if(isEventAnnotated() && eventClass.getAnnotation(Event.class).listener() != EventListener.class) {
            throw new InvalidEventConfigurationException("The Event " + eventClass + " has already a bound interface via its @Event annotation");
        }
        this.interfaceClass = interfaceClass;
        this.methodResolver = methodResolver;
        return this;
    }
    
   
    @Override
    public EventBuilder bindAnnotation(Class<? extends Annotation> annotationClass) throws InvalidEventConfigurationException {
        if(this.annotationClass != null){
            throw new InvalidEventConfigurationException("An Event can only be bound to one Annotation once");
        }
        if(isEventAnnotated() && eventClass.getAnnotation(Event.class).annotation() != Event.class) {
            throw new InvalidEventConfigurationException("The Event " + eventClass + " has already a bound interface via its @Event annotation");
        }
        this.annotationClass = annotationClass;
        return this;
    }
    
    /**
     * Set a specific event queue to handle these events.
     * 
     * @param eventQueueName Name of event queue to handle events.
     * 
     * @return This builder to allow chaining.
     **/
    @Override
    public EventBuilder eventQueue(String eventQueueName) {
       this.eventQueue = eventQueueName;
       return this;
    }
    
   
    @Override
    public EventConfig build() {
        if(name == null) {
            name = eventClass.getName();
        }
       
        if(isEventAnnotated()) {
            return buildByEventAnnotation();
        } else {
            return buildByExplicitConfiguration();
        }
       
    }
    
    private boolean isEventAnnotated() {
        return eventClass.isAnnotationPresent(Event.class);
    }
    
    private EventConfig buildByEventAnnotation() throws InvalidEventConfigurationException{
        Event eventAnnotation = eventClass.getAnnotation(Event.class);
        if(eventAnnotation.listener() == EventListener.class && eventAnnotation.annotation() == Event.class) {
            throw new InvalidEventConfigurationException("The even " + eventClass + " needs to have either a listener or an annotation attribute set in the @Event annotation");
        }
        EventConfigImpl eventConfig = new EventConfigImpl(eventClass, name);
        if(eventAnnotation.listener() != EventListener.class) {
            interfaceClass = eventClass.getAnnotation(Event.class).listener();
        }
        if(interfaceClass != null) {
            eventConfig.setListener(interfaceClass);
            MethodResolving methodResolving = interfaceClass.getAnnotation(MethodResolving.class);
            if (methodResolving != null) {
                eventConfig.setMethodResolver(methodResolverExtractor.getMethodResolver(eventClass, interfaceClass, methodResolving));
            } else {
                setDefaultMethod(eventConfig, eventClass, interfaceClass);
            }
        }
        
        addAnnotationBinding(eventConfig);
        addEventQueue(eventConfig, eventAnnotation.eventQueue());
        return eventConfig;
    }
    
    private EventConfig buildByExplicitConfiguration() {
        EventConfigImpl eventConfig = new EventConfigImpl(eventClass, name);
        if(interfaceClass != null) {
            eventConfig.setListener(interfaceClass);
            if(eventMethod != null) {
                MethodUtils.verifyMethodSignature(eventMethod, Void.TYPE, eventClass);
                eventConfig.setDefaultMethod(eventMethod);
            } else {
                eventConfig.setMethodResolver(methodResolver);
            }
        }
        addAnnotationBinding(eventConfig);
        addEventQueue(eventConfig, eventQueue);
        return eventConfig;
    }
    
    private void addAnnotationBinding(EventConfigImpl eventConfig) {
        
        if(isEventAnnotated() && eventClass.getAnnotation(Event.class).annotation() != Event.class) {
            annotationClass = eventClass.getAnnotation(Event.class).annotation();
         }
         if(annotationClass != null) {
             validateTargetAnnotation(annotationClass);
             AnnotatedMethodMethodResolver methodResolver = new AnnotatedMethodMethodResolver(annotationClass, eventConfig.getMethodResolver());
             eventConfig.setMethodResolver(methodResolver);
             eventConfig.setEventAnnotation(annotationClass);
         }
    }
    
    private void addEventQueue(EventConfigImpl eventConfig, String eventQueueName) {
        if(eventQueueName != null && !Event.DEFAULT_EVENT_QUEUE_NAME.equals(eventQueueName)) {
           try {
               DispatcherQueueThread eventQueue = eventQueueRegister.get(eventQueueName);
               eventConfig.setEventQueue(eventQueue);
           } catch(IllegalArgumentException e) {   
               throw new IllegalStateException("Event Queue named " + eventQueueName + " for event "+ eventClass + " could not be found.");
           }
           
        }
    }
    
    /**
     * Find the default method to invoke on listener and sets it on the eventConfig.
     * 
     * @param eventConfig
     *            The EventConfig.
     * @param event
     *            Event class
     * @param listener
     *            Event Listener Interface class
     * 
     **/
    private void setDefaultMethod(EventConfigImpl eventConfig, Class<? extends EventObject> event,
                Class<? extends EventListener> listener) {
        if (MethodUtils.getDeclaredMethodCount(listener) == 1) {
            Set<Method> methodSet = MethodUtils.getDeclaredMethods(listener, new MethodFilterList(new ReturnTypeMethodFilter(Void.TYPE), new SignatureMethodFilter(event)));
            eventConfig.setDefaultMethod(methodSet.iterator().next());
        } else {
            throw new IllegalStateException("EventListener " + listener
                        + " should only have one method declared if not annotated with @MethodResolving");
        }
    }
    
    /**
     * Validates that the target annotation is a valid annotation for a event method annotation.
     * 
     * @param targetAnnotation
     * @throws IllegalStateException if target annotation does not have Retention.RUNTIME and Target does not include ElementType.METHOD.
     **/
    private void validateTargetAnnotation(Class<? extends Annotation> targetAnnotation) throws IllegalStateException{
        Retention annotationRetention = targetAnnotation.getAnnotation(Retention.class);
        if(annotationRetention == null || annotationRetention.value() != RetentionPolicy.RUNTIME) {
            throw new IllegalStateException(targetAnnotation + " must have use retention policy RUNTIME to be used as an Event Method Annotation");
        }
        Target annotationTarget = targetAnnotation.getAnnotation(Target.class);
        boolean methodTarget = false;
        if(annotationTarget != null) {
            ElementType[] elemntTypes = annotationTarget.value();
            for (ElementType type : elemntTypes) {
                if(type == ElementType.METHOD) {
                    methodTarget = true;
                    break;
                }
            }
        }
        if(!methodTarget) {
            throw new IllegalStateException("It must be possible to annotate a methd with " + targetAnnotation + " in order to use it as a Event Method Annotation, ensure that the @Target annotation has ElementType.METHOD set.");
        }
    }


    
}
