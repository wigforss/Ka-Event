package org.kasource.kaevent.event.method.switchcase;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.event.config.InvalidEventConfigurationException;
import org.kasource.kaevent.event.method.MethodResolver;

/**
 * A Method Resolver that works like a switch statement.
 * 
 * The Event class provides a keyword method and the Listener Interface provides the cases (one method per case).
 * <p>
 * <b>Usage without annotation</b>
 * If the event and interface class is not annotated the second constructor 
 * can be used which contains the data normally extracted from the annotations.
 * <p>
 * <b>Configuration by annotations</b>
 * 
 * The Event Class should have a no argument method annotated with @EventKeyword<p>
 * 
 * The Event Interface should have one (and only one) method annotated 
 * with @DefaultListenerMethod which will be invoked 
 * if nothing matches the keyword. Each method can then be annotated 
 * with @KeywordCase(String) which value will be matched against the
 * result of invoking the @EventKeyword method on the event itself.
 * <p>
 *  
   <pre>
   {@code
    enum CrudAction  {CREATE, READ, UPDATE, DELETE};
    
    @Event(listener=CrudEventListener.class)
    public class CrudEvent extends EventObject {
        
        private CrudAction action;
        
       public  CrudEvent(Object source, CrudAction action) {
           super(source);
           this.action = action;
       }
       
       @EventKeyword
       public CrudAction getAction() {
           return action;
       }
    }
   
    @MethodResolving(MethodResolverType.KEYWORD_SWITCH)
    public interface CrudEventListener extends EventListener{
        
        @KeywordCase("CREATE")
        public void createEvent(CrudEvent event);
        
        @DefaultListenerMethod
        @KeywordCase("READ")
        public void readEvent(CrudEvent event);
        
        @KeywordCase("UPDATE")
        public void updateEvent(CrudEvent event);
        
        @KeywordCase("DELETE")
        public void deleteEvent(CrudEvent event);
    }
    }
    </pre>
  <p>
  As seen in the example the value return from getAction will be matched with the @Keyword case values. 
  When a create event is fired the createEvent method will be returned from this method resolver. One could
  argue that misspelling of the cases will cause the default method to be called instead of the intended one, 
  which can be hard to detect. To solve this creation of custom case annotations is allowed, see example below.
  <p>
  <pre>
  {@code
  @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @CustomCase
    public @interface CrudCase {
        CrudAction value();
    }
    
    @MethodResolving(MethodResolverType.KEYWORD_SWITCH)
    public interface CrudEventListener2 extends EventListener{
        
        @CrudCase(CrudAction.CREATE)
        public void createEvent(CrudEvent event);
        
        @DefaultListenerMethod
        @CrudCase(CrudAction.READ)
        public void readEvent(CrudEvent event);
        
        @CrudCase(CrudAction.UPDATE)
        public void updateEvent(CrudEvent event);
        
        @CrudCase(CrudAction.DELETE)
        public void deleteEvent(CrudEvent event);
    }
    }
    </pre>
 * 
 * 
 * 
 * @author wigforss
 * @version $Id$
 **/
public class KeywordSwitchMethodResolver implements MethodResolver<EventObject> {

    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private Method defaultMethod;
    private Method eventKeywordMethod; // May be null

  
    /**
     * Constructor used with annotated classes.
     * 
     * @param eventClass                Event class
     * @param listenerClass             Listener Interface class
     **/
    public KeywordSwitchMethodResolver(Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) {
        new KeywordSwitchAnnotationConfigurer(this, eventClass, listenerClass).configure();
        new KeywordSwitchVerifier().verify(this, eventClass, listenerClass);
    }

    /**
     * Constructor used when no annotations is present.
     * 
     * @param eventClass                
     *              Event class
     * @param listenerClass             
     *              Listener Interface class
     * @param defaultMethodName             
     *              Default method to invoke
     * @param methodNameMap                 
     *              Map<Keyword, Method> that is matched against the result of invoking the <i>eventKeywordMethod</i>
     * @param eventKeywordMethodName        
     *              Method on event class that provides the keyword
     **/
    public KeywordSwitchMethodResolver(Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass, 
            String defaultMethodName, 
            Map<String, String> methodNameMap,
            String eventKeywordMethodName) {
        this.defaultMethod = getListenerMethod(eventClass, listenerClass, defaultMethodName);
        setMethodMap(eventClass, listenerClass, methodNameMap);
        this.eventKeywordMethod = getEventKeywordMethod(eventClass, eventKeywordMethodName);
        new KeywordSwitchVerifier().verify(this, eventClass, listenerClass);
    }

    /**
     * Returns the event keyword method from the eventClass.
     * 
     * @param eventClass                Event Class.
     * @param eventKeywordMethodName    Name of the Event keyword method.
     * 
     * @return the event keyword method.
     **/
    private Method getEventKeywordMethod(Class<? extends EventObject> eventClass, String eventKeywordMethodName) {
        Method method = ReflectionUtils.getDeclaredMethod(eventClass, eventKeywordMethodName);
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new InvalidEventConfigurationException("The method " + eventKeywordMethodName 
                        + " in class " + eventClass + " must return value");
        }
        return method;
    }
    
    /**
     * Returns the named listener method from the listenerClass.
     * 
     * @param eventClass     Event Class.
     * @param listenerClass  Listener Interface Class.
     * @param methodName     Name of the method.
     * 
     * @return The named listener method.
     * 
     * @throws InvalidEventConfigurationException if the method with methodName is not a void method.
     **/
    private Method getListenerMethod(Class<? extends EventObject> eventClass, 
                                     Class<? extends EventListener> listenerClass, 
                                     String methodName) throws InvalidEventConfigurationException {
        Method method = ReflectionUtils.getDeclaredMethod(listenerClass, methodName, eventClass);
        if (!method.getReturnType().equals(Void.TYPE)) {
            throw new InvalidEventConfigurationException("The method " + methodName + " in class " 
                        + listenerClass + " should not have a return value");
        }
       return method;
    }
    
    /**
     * Set method map from string and lookup the actual Methods. 
     * 
     * @param eventClass     Event Class.
     * @param listenerClass  Event Listener Interface. 
     * @param methodNameMap  Method map to set.
     **/
    private void setMethodMap(Class<? extends EventObject> eventClass, 
                              Class<? extends EventListener> listenerClass, 
                              Map<String, String> methodNameMap) {
        for (Map.Entry<String, String> methodNameEntry : methodNameMap.entrySet()) {
            Method method = getListenerMethod(eventClass, listenerClass, methodNameEntry.getValue());
            methodMap.put(methodNameEntry.getKey(), method);
        }
    }
 
    @Override
    public Method resolveMethod(EventObject event) {

        Object keyword = null;
        try {
            keyword = eventKeywordMethod.invoke(event, new Object[] {});
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke " + eventKeywordMethod.getName() + "");
        }
        if (keyword == null) {
            return defaultMethod;
        }
        Method method = defaultMethod;
        if (methodMap != null) {
            method = methodMap.get(keyword.toString());
        }
        if (method == null) {
            return defaultMethod;
        }
        return method;
    }

    /**
     * @return the methodMap
     */
    Map<String, Method> getMethodMap() {
        return methodMap;
    }

    /**
     * @param methodMap the methodMap to set
     */
    void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }

    /**
     * @return the defaultMethod
     */
    Method getDefaultMethod() {
        return defaultMethod;
    }

    /**
     * @param defaultMethod the defaultMethod to set
     */
    void setDefaultMethod(Method defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    /**
     * @return the eventKeywordMethod
     */
    Method getEventKeywordMethod() {
        return eventKeywordMethod;
    }

    /**
     * @param eventKeywordMethod the eventKeywordMethod to set
     */
    void setEventKeywordMethod(Method eventKeywordMethod) {
        this.eventKeywordMethod = eventKeywordMethod;
    }

   
}


