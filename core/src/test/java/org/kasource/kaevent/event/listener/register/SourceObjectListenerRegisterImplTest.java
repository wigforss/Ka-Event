/**
 * 
 */
package org.kasource.kaevent.event.listener.register;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static org.easymock.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
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
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SourceObjectListenerRegisterImplTest {

    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private BeanResolver beanResolver;
    
    @Mock
    private EventConfig eventConfig;
    
    
    @TestedObject
    private SourceObjectListenerRegisterImpl register = 
        new SourceObjectListenerRegisterImpl(eventRegister, beanResolver);
    
    @SuppressWarnings("rawtypes")
    @Test
    public void registerListenerTest() {
        Object source = new Object();
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
            }
        };
        EventListenerRegistration listenerReg = new EventListenerRegistration(listener, null);
        expect(eventRegister.hasEventByInterface(ChangeListener.class)).andReturn(true);
        expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig);
        expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        expect(eventRegister.getRegisteredEventAnnotations()).andReturn(Collections.EMPTY_SET);
        EasyMockUnitils.replay();
        
        register.registerListener(listener, source);
        Collection<EventListenerRegistration> eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(1, eventListeners.size());
        assertEquals(listenerReg, eventListeners.iterator().next());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void unregisterListenerTest() {
        Object source = new Object();
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EventListenerRegistration listenerReg = new EventListenerRegistration(listener, null);
        expect(eventRegister.hasEventByInterface(ChangeListener.class)).andReturn(true);
        expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig);
        expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        expect(eventRegister.getRegisteredEventAnnotations()).andReturn(Collections.EMPTY_SET);
        EasyMockUnitils.replay();
        
        register.registerListener(listener, source);
        Collection<EventListenerRegistration> eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(1, eventListeners.size());
        assertEquals(listenerReg, eventListeners.iterator().next());
        register.unregisterListener(listener, source);
        eventListeners = register.getListenersByEvent(new ChangeEvent(source));
        assertEquals(0, eventListeners.size());
    }
}
