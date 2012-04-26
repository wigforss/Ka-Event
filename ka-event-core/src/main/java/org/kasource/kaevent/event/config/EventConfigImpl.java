package org.kasource.kaevent.event.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.method.MethodResolver;



/**
 * Holds information on how an event has been configured.
 * 
 * @author rikard
 * @version $Id$
 **/
public class EventConfigImpl implements EventConfig {
    private Class<? extends EventObject> eventClass;
    private Class<? extends EventListener> listener;
    private Class<? extends Annotation> eventAnnotation;
    private String name;
    private Method defaultMethod;  
    @SuppressWarnings({ "rawtypes" })
    private MethodResolver methodResolver; 
    private DispatcherQueueThread eventQueue;
    
    /**
     * Constructor.
     * 
     * @param eventClass	Event class.
     * @param listener		Event Listener interface class.
     * @param name			Name of the event.
     **/
    EventConfigImpl(Class<? extends EventObject> eventClass /*, 
                           Class<? extends EventListener> listener */,
                           String name) {
        this.eventClass = eventClass;
      //  this.listener = listener;
        this.name = name;
    }
    
    /**
     * Constructor.
     * 
     * @param eventClass    Event class.
     * @param listener      Event Listener interface class.
     * @param name          Name of the event.
     **/
   /* EventConfigImpl(Class<? extends EventObject> eventClass, 
                           AnnotatedMethodMethodResolver methodResolver,
                           String name) {
        this.eventClass = eventClass;
        this.methodResolver = methodResolver;
        this.name = name;
        this.eventAnnotation = methodResolver.getTargetAnnotation();
    }
*/
    

    /**
     * Return the listener class associated with the eventClass.
     * 
     * @return the listener interface class
     */
    public Class<? extends EventListener> getListener() {
        return listener;
    }

    

    /**
     * Returns the interface method to be invoked, may be null if runtime
     * strategy method for resolve method is used.
     * 
     * @param event Event object.
     * 
     * @return Returns the interface method to be invoked
     **/
    @SuppressWarnings("unchecked")
    @Override
    public  Method getEventMethod(EventObject event, Object target) {
        Method method = null;
        if(methodResolver != null) {
            method = methodResolver.resolveMethod(event, target);
        }
        if(method == null) {
            method = defaultMethod;
        }
        return method;
        
    }

    
    @SuppressWarnings("rawtypes")
    MethodResolver getMethodResolver() {
        return methodResolver;
    }
    
    /**
     * Return the actual event class.
     * 
     * @return the event class
     **/
    @Override
    public Class<? extends EventObject> getEventClass() {
        return eventClass;
    }



    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }


    /**
     * @param methodResolver the methodResolver to set
     */
    @SuppressWarnings("rawtypes")
    void setMethodResolver(MethodResolver methodResolver) {
        this.methodResolver = methodResolver;
    }

   

    /**
     * @param defaultMethod the defaultMethod to set
     */
    void setDefaultMethod(Method defaultMethod) {
        this.defaultMethod = defaultMethod;
    }
    
    void setEventAnnotation(Class<? extends Annotation> eventAnnotation) {
        this.eventAnnotation = eventAnnotation;
    }

    /**
     * Returns the event method annotation in use.
     * 
     * @return the event method annotation in use, null if no annotation is used for this Event.
     **/
    public Class<? extends Annotation> getEventAnnotation() {
        return eventAnnotation;
    }

    /**
     * @param listener the listener to set
     */
     void setListener(Class<? extends EventListener> listener) {
        this.listener = listener;
    }

    /**
     * @return the eventQueue
     */
    public DispatcherQueueThread getEventQueue() {
        return eventQueue;
    }

    /**
     * @param eventQueue the eventQueue to set
     */
     void setEventQueue(DispatcherQueueThread eventQueue) {
        this.eventQueue = eventQueue;
    }

}
