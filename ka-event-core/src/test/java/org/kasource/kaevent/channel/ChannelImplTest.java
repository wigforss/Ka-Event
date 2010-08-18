/**
 * 
 */
package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author wigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ChannelImplTest {
    
    @InjectIntoByType
    private String name = "testChannel";
    
    @InjectIntoByType
    @Mock
    private ChannelListenerRegister listenerRegister;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private EventMethodInvoker eventMethodInvoker;
    
    @InjectIntoByType
    @Mock
    private BeanResolver beanResolver;
   
    
    
    @Mock
    private EventConfig eventConfig;
    
    @Mock
    private Set<EventListenerRegistration> listeners;
    
    @TestedObject
    private ChannelImpl channel = new ChannelImpl(name, channelRegister, eventRegister, eventMethodInvoker, beanResolver);
    
    @SuppressWarnings("unchecked")
    @Test
    public void registerEventTest() {
        expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        expect((Class)eventConfig.getListener()).andReturn(ChangeListener.class);
        channelRegister.handleEvent(channel, ChangeEvent.class);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.registerEvent(ChangeEvent.class);
        assertEquals( ChangeEvent.class, channel.getEvents().iterator().next() );
        assertEquals( ChangeListener.class, channel.getSupportedInterfaces().iterator().next() );
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void registerEventTwiceTest() {
        expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig).times(2);
        expect((Class)eventConfig.getListener()).andReturn(ChangeListener.class);
        channelRegister.handleEvent(channel, ChangeEvent.class);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.registerEvent(ChangeEvent.class);
        channel.registerEvent(ChangeEvent.class);
        assertEquals( ChangeEvent.class, channel.getEvents().iterator().next() );
        assertEquals( ChangeListener.class, channel.getSupportedInterfaces().iterator().next() );
    }
    
    @Test(expected=IllegalStateException.class)
    public void registerUnsupportedEventTest() {
        expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(null);
        EasyMockUnitils.replay();
        channel.registerEvent(ChangeEvent.class);
    }
    
    
    @Test
    public void fireEventTest() {
        ChangeEvent event = new ChangeEvent("source");
        expect(listenerRegister.getListenersByEvent(event)).andReturn(listeners);
        eventMethodInvoker.invokeEventMethod(event,listeners,false);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.fireEvent(event, false);
    }
    
    @Test
    public void nameTest() {
        channel.setName("name");
        assertEquals("name", channel.getName());
    }
    
    @Test
    public void registerListenerTest() {
        
        ChangeListener listener = new ChangeListener() {
          ///CLOVER:OFF 
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
            ///CLOVER:ON
        };
        listenerRegister.registerListener(listener);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.registerListener(listener);
    }
    
    @Test
    public void unregisterListenerTest() {
        ChangeListener listener = new ChangeListener() {
            ///CLOVER:OFF 
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }
          ///CLOVER:ON
        };
        listenerRegister.unregisterListener(listener);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.unregisterListener(listener);
    }
}
