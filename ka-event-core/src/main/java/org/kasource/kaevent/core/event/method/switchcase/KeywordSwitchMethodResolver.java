package org.kasource.kaevent.core.event.method.switchcase;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.core.event.method.MethodResolver;

/**
 * A Method Resolver that works like a switch statement.
 * 
 * The Event class provides a keyword method and the Listener Interface provides the cases (one method per case).
 * <p>
 * <b>Usage without annotation</b>
 * If the event and interface class is not annotated the second constructor can be used which contains the data normally extracted from 
 * the annotations.
 * <p>
 * <b>Configuration by annotations</b>
 * 
 * The Event Class should have a no argument method annotated with @EventKeyword<p>
 * 
 * The Event Interface should have one (and only one) method annotated with @DefaultListenerMethod which will be invoked 
 * if nothing matches the keyword. Each method can then be annotated with @KeywordCase(String) which value will be matched against the
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

     Map<String, Method> methodMap = new HashMap<String, Method>();
     Method defaultMethod;
     Method eventKeywordMethod; // May be null

  
    /**
     * Constructor used with annotated classes
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
     * Constructor used when no annotations is present
     * 
     * @param eventClass                Event class
     * @param listenerClass             Listener Interface class
     * @param defaultMethod             Default method to invoke
     * @param methodMap                 Map<Keyword, Method> that is matched against the result of invoking the <i>eventKeywordMethod</i>
     * @param eventKeywordMethod        Method on event class that provides the keyword
     **/
    public KeywordSwitchMethodResolver(Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass, Method defaultMethod, Map<String, Method> methodMap,
            Method eventKeywordMethod) {
        this.defaultMethod = defaultMethod;
        this.methodMap = methodMap;
        this.eventKeywordMethod = eventKeywordMethod;
        new KeywordSwitchVerifier().verify(this, eventClass, listenerClass);
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
     * @param methodMap the methodMap to set
     */
    public void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }

    /**
     * @param defaultMethod the defaultMethod to set
     */
    public void setDefaultMethod(Method defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    /**
     * @param eventKeywordMethod the eventKeywordMethod to set
     */
    public void setEventKeywordMethod(Method eventKeywordMethod) {
        this.eventKeywordMethod = eventKeywordMethod;
    }
}


