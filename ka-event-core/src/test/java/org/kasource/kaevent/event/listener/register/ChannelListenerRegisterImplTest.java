/**
 * 
 */
package org.kasource.kaevent.event.listener.register;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.EventListener;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegisterImpl;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
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
public class ChannelListenerRegisterImplTest {

    @InjectIntoByType
    @Mock
    private Channel channel;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private BeanResolver beanResolver;
    
    @Mock
    private EventConfig eventConfig;
    
    @Mock
    private Set<? extends EventListener> events;
    
    @TestedObject
    private ChannelListenerRegisterImpl register = new ChannelListenerRegisterImpl(channel, eventRegister, beanResolver);
    
    
    @SuppressWarnings("unchecked")
    @Test
    public void registerListenerTest() {
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EventListenerRegistration listenerReg = new EventListenerRegistration(listener, null);
      
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig).times(2);
        EasyMock.expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        EasyMock.expect((Collection) channel.getSupportedInterfaces()).andReturn(events);
        EasyMock.expect(events.contains(ChangeListener.class)).andReturn(true);
        EasyMockUnitils.replay();
        register.registerListener(listener);
        Collection<EventListenerRegistration> listeners = register.getListenersByEvent(new ChangeEvent("Hej"));
        assertEquals(1, listeners.size());
        assertEquals(listenerReg, listeners.iterator().next());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void unregisterListenerTest() {
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EventListenerRegistration listenerReg = new EventListenerRegistration(listener, null);
      
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig).times(2);
        EasyMock.expect((Class)eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        EasyMock.expect((Collection)channel.getSupportedInterfaces()).andReturn(events);
        EasyMock.expect(events.contains(ChangeListener.class)).andReturn(true);
        EasyMockUnitils.replay();
        register.registerListener(listener);
        Collection<EventListenerRegistration> listeners = register.getListenersByEvent(new ChangeEvent("Hej"));
        assertEquals(1, listeners.size());
        assertEquals(listenerReg, listeners.iterator().next());
        register.unregisterListener(listener);
        listeners = register.getListenersByEvent(new ChangeEvent("Hej"));
        assertEquals(0, listeners.size());
    }
    
    
    
    /*
    @SuppressWarnings("unchecked")
    @Test
    public void registerListenerTest() {
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig);
        EasyMock.expect((Class)eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(1);
        EasyMock.expect((Set)channel.getEvents()).andReturn(events);
        EasyMock.expect(events.contains(ChangeEvent.class)).andReturn(true);
        EasyMockUnitils.replay();
        register.registerListener(listener);
        Set<EventListener> listeners = register.getListenersByEventClass(ChangeEvent.class);
        assertEquals(1, listeners.size());
        assertEquals(listener, listeners.iterator().next());
    }
    */
    /*
    @SuppressWarnings("unchecked")
    @Test
    public void unRegisterListenerTest() {
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        EasyMock.expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig).times(2);
        EasyMock.expect((Class)eventConfig.getEventClass()).andReturn(ChangeEvent.class).times(2);
        EasyMock.expect((Set)channel.getEvents()).andReturn(events).times(2);
        EasyMock.expect(events.contains(ChangeEvent.class)).andReturn(true).times(2);
        EasyMockUnitils.replay();
        register.registerListener(listener);
        Set<EventListener> listeners = register.getListenersByEventClass(ChangeEvent.class);
        assertEquals(1, listeners.size());
        assertEquals(listener, listeners.iterator().next());
        register.unregisterListener(listener);
        listeners = register.getListenersByEventClass(ChangeEvent.class);
        assertEquals(0, listeners.size());
    }
    */
}