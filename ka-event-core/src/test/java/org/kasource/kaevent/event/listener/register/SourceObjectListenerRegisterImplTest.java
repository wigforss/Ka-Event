/**
 * 
 */
package org.kasource.kaevent.event.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SourceObjectListenerRegisterImplTest {

    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @Mock
    private EventConfig eventConfig;
    
    
    @TestedObject
    private SourceObjectListenerRegisterImpl register = new SourceObjectListenerRegisterImpl(eventRegister);
    
    @Test
    public void registerListenerTest() {
        Object source = new Object();
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig).times(2);
        EasyMock.expect((Class)eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        EasyMockUnitils.replay();
        
        register.registerListener(listener, source);
        Set<EventListener> eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(1, eventListeners.size());
        assertEquals(listener, eventListeners.iterator().next());
    }
    
    @Test
    public void unregisterListenerTest() {
        Object source = new Object();
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig).times(2);
        EasyMock.expect((Class)eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        EasyMockUnitils.replay();
        
        register.registerListener(listener, source);
        Set<EventListener> eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(1, eventListeners.size());
        assertEquals(listener, eventListeners.iterator().next());
        register.unregisterListener(listener, source);
        eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(0, eventListeners.size());
    }
}
