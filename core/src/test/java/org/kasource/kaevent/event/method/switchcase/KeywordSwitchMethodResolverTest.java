/**
 * 
 */
package org.kasource.kaevent.event.method.switchcase;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.annotations.event.EventKeyword;
import org.kasource.kaevent.annotations.event.methodresolving.CustomCase;
import org.kasource.kaevent.annotations.event.methodresolving.DefaultListenerMethod;
import org.kasource.kaevent.annotations.event.methodresolving.KeywordCase;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.mock.annotation.Dummy;


/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class KeywordSwitchMethodResolverTest {

    @Dummy
    private Object target;
    
    
	@Test
	public void resolverMethodTest() throws SecurityException, NoSuchMethodException {
		KeywordSwitchMethodResolver resolver = 
		    new KeywordSwitchMethodResolver(CrudEvent.class, CrudEventListener.class);
		
		Method method = resolver.resolveMethod(new CrudEvent("Test", CrudAction.DELETE), target);
		assertEquals(CrudEventListener.class.getDeclaredMethod("deleteEvent", CrudEvent.class), method);
	}
	
	
    enum CrudAction  { CREATE, READ, UPDATE, DELETE };
    
    @SuppressWarnings("serial")
    @Event(listener = CrudEventListener.class)
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
    public interface CrudEventListener extends EventListener {
        
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
    public interface CrudEventListener2 extends EventListener {
        
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
