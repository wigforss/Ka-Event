/**
 * 
 */
package org.kasource.kaevent.core.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.core.event.Event;
import org.kasource.kaevent.core.event.EventKeyword;
import org.kasource.kaevent.core.listener.interfaces.CustomCase;
import org.kasource.kaevent.core.listener.interfaces.DefaultListenerMethod;
import org.kasource.kaevent.core.listener.interfaces.KeywordCase;
import org.kasource.kaevent.core.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.core.listener.interfaces.MethodResolving;

/**
 * @author rikardwigforss
 *
 */
public class KeywordSwitchMethodResolverTest {

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
