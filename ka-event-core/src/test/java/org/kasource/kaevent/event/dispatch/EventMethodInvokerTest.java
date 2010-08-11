/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.InjectIntoStaticByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.PropertyAccess;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventMethodInvokerTest {

    @InjectIntoStaticByType(propertyAccess=PropertyAccess.FIELD, target=EventMethodInvoker.class)
    @Mock
    private Logger logger;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;

    @Mock
    private EventConfig eventConfig;
    
    @TestedObject
    private EventMethodInvoker eventMethodInvoker = new EventMethodInvoker(eventRegister);
    
    
    
    @Test
    public void invokeMethodTest() throws SecurityException, NoSuchMethodException {
        Capture<Boolean> methodInvoked = new Capture<Boolean>();
        
        class MyChangeListener implements ChangeListener {
            private Capture<Boolean> capture;
            public MyChangeListener(Capture<Boolean> capture) {
                this.capture = capture;
            }
            public void stateChanged(ChangeEvent e) {
                capture.setValue(Boolean.valueOf(true));       
             }
        }
        ChangeListener listener = new MyChangeListener(methodInvoked); 
        Set<EventListener> listeners = new HashSet<EventListener>();
        listeners.add(listener);
        EventObject event = new ChangeEvent("Test");
       
        EasyMock.expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        EasyMock.expect(eventConfig.getEventMethod(event)).andReturn(ChangeListener.class.getDeclaredMethod("stateChanged", ChangeEvent.class));
        EasyMockUnitils.replay();
        eventMethodInvoker.invokeEventMethod(event, listeners, false);
        assertEquals(Boolean.TRUE, methodInvoked.getValue());
    }
    
    @Test
    public void invokeMethodExceptionTest() throws SecurityException, NoSuchMethodException {
        ChangeListener listener = new ChangeListener() {        
            @Override
            public void stateChanged(ChangeEvent e) {
                throw new RuntimeException("Test exception");              
            }
        };
        Set<EventListener> listeners = new HashSet<EventListener>();
        listeners.add(listener);
        EventObject event = new ChangeEvent("Test");
       
        EasyMock.expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        EasyMock.expect(eventConfig.getEventMethod(event)).andReturn(ChangeListener.class.getDeclaredMethod("stateChanged", ChangeEvent.class));
        logger.error(EasyMock.isA(String.class), EasyMock.isA(RuntimeException.class));
        EasyMockUnitils.replay();
        eventMethodInvoker.invokeEventMethod(event, listeners, false);
       
    }
    
    @Test
    public void invokeMethodBlockedTest() throws SecurityException, NoSuchMethodException {
        Capture<Boolean> methodInvoked = new Capture<Boolean>();
        
        class MyChangeListener implements ChangeListener {
            private Capture<Boolean> capture;
            public MyChangeListener(Capture<Boolean> capture) {
                this.capture = capture;
            }
            public void stateChanged(ChangeEvent e) {
                capture.setValue(Boolean.valueOf(true));       
             }
        }
        ChangeListener listener = new MyChangeListener(methodInvoked); 
        Set<EventListener> listeners = new HashSet<EventListener>();
        listeners.add(listener);
        EventObject event = new ChangeEvent("Test");
       
        EasyMock.expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        EasyMock.expect(eventConfig.getEventMethod(event)).andReturn(ChangeListener.class.getDeclaredMethod("stateChanged", ChangeEvent.class));
        EasyMockUnitils.replay();
        eventMethodInvoker.invokeEventMethod(event, listeners, true);
        assertEquals(Boolean.TRUE, methodInvoked.getValue());
    }
    
    @Test(expected=RuntimeException.class)
    public void invokeMethodBlockedExceptionTest() throws SecurityException, NoSuchMethodException {
       
        ChangeListener listener = new ChangeListener() {        
            @Override
            public void stateChanged(ChangeEvent e) {
                throw new RuntimeException("Test exception");              
            }
        }; 
        Set<EventListener> listeners = new HashSet<EventListener>();
        listeners.add(listener);
        EventObject event = new ChangeEvent("Test");
       
        EasyMock.expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        EasyMock.expect(eventConfig.getEventMethod(event)).andReturn(ChangeListener.class.getDeclaredMethod("stateChanged", ChangeEvent.class));
        EasyMockUnitils.replay();
        eventMethodInvoker.invokeEventMethod(event, listeners, true);
        
    }
    
    @Test
    public void invokeMethodNoListenersTest() throws SecurityException, NoSuchMethodException {       
        Set<EventListener> listeners = new HashSet<EventListener>();
        EventObject event = new ChangeEvent("Test");  
        eventMethodInvoker.invokeEventMethod(event, listeners, false);  
    }
    
    @Test
    public void invokeMethodNullListenersTest() throws SecurityException, NoSuchMethodException {       
        EventObject event = new ChangeEvent("Test");  
        eventMethodInvoker.invokeEventMethod(event, null, false);  
    }
}
