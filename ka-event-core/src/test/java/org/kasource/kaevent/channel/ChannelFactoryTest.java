/**
 * 
 */
package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
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
public class ChannelFactoryTest {

    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private EventMethodInvoker eventMethodInvoker;
    
    @InjectIntoByType
    @Mock
    private BeanResolver beanResolver;
    
    @Mock
    private EventConfig eventConfig;

    
    @TestedObject
    private ChannelFactory factory = new ChannelFactoryImpl(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
    
    @Test
    public void createChannelTest() {
        Capture<ListenerChannel> capturedChannel = new Capture<ListenerChannel>();
        expect(channelRegister.getChannel("testChannel")).andThrow(new NoSuchChannelException("test"));
        channelRegister.registerChannel(EasyMock.capture(capturedChannel));
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        Channel channel = factory.createChannel("testChannel");
        assertEquals(channel, capturedChannel.getValue());
        assertEquals("testChannel", channel.getName());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void createChannelWithEventsTest() {
        Capture<ListenerChannel> capturedChannel = new Capture<ListenerChannel>();
        Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
        events.add(ChangeEvent.class);
        expect(channelRegister.getChannel("testChannel")).andThrow(new NoSuchChannelException("test"));
        expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
        expect((Class) eventConfig.getListener()).andReturn(ChangeListener.class);
        channelRegister.registerEventHandler(EasyMock.capture(capturedChannel), EasyMock.same(ChangeEvent.class));
        expectLastCall();
        channelRegister.registerChannel(EasyMock.capture(capturedChannel));
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        Channel channel = factory.createChannel(ChannelImpl.class, "testChannel", events);
        assertEquals(channel, capturedChannel.getValue());
        assertEquals("testChannel", channel.getName());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void createChannelWithEventsAndListenersTest() {
        Capture<ListenerChannel> capturedChannel = new Capture<ListenerChannel>();
        Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
        events.add(ChangeEvent.class);
        ChangeListener listener = new ChangeListener() {
            ///CLOVER:OFF 
              @Override
              public void stateChanged(ChangeEvent e) {
                  // TODO Auto-generated method stub
                  
              }
              ///CLOVER:ON
          };
        Set<EventListener> listeners = new HashSet<EventListener>();
        listeners.add(listener);
        expect(channelRegister.getChannel("testChannel")).andThrow(new NoSuchChannelException("test"));
        expect(eventRegister.getEventByClass(ChangeEvent.class)).andReturn(eventConfig);
       
       
        expect((Class) eventConfig.getListener()).andReturn(ChangeListener.class);
        channelRegister.registerEventHandler(EasyMock.capture(capturedChannel), EasyMock.same(ChangeEvent.class));
        expectLastCall();
        channelRegister.registerChannel(EasyMock.capture(capturedChannel));
        expectLastCall();
        expect(eventRegister.hasEventByInterface(ChangeListener.class)).andReturn(true);
        expect(eventRegister.getEventByInterface(ChangeListener.class)).andReturn(eventConfig);
        expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class);
        expect(eventRegister.getRegisteredEventAnnotations()).andReturn(Collections.EMPTY_SET);
        EasyMockUnitils.replay();
        ListenerChannel channel = factory.createChannel(ChannelImpl.class, "testChannel", events, listeners);
        assertEquals(channel, capturedChannel.getValue());
        assertEquals("testChannel", channel.getName());
    }
}
