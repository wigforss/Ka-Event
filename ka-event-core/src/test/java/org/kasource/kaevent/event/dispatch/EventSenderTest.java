/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
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
public class EventSenderTest {
    
    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private EventListenerRegister sourceObjectListenerRegister;
    
    @InjectIntoByType
    @Mock
    private EventMethodInvoker invoker;
    
    @Mock
    private Channel channel;
    
    @Mock
    private Set<EventListenerRegistration> listenerSet;
    
    @TestedObject
    private EventSender eventSender = new EventSender(channelRegister, sourceObjectListenerRegister, invoker);
    
    @Test
    public void dispatchEventTest() {
        Set<Channel> channelSet = new HashSet<Channel>();
        channelSet.add(channel);
       
        EventObject event = new ChangeEvent("Test");
        EasyMock.expect(channelRegister.getChannelsByEvent(ChangeEvent.class)).andReturn(channelSet);
        channel.fireEvent(event, false);
        EasyMock.expectLastCall();
        EasyMock.expect(sourceObjectListenerRegister.getListenersByEvent(event)).andReturn(listenerSet);
        invoker.invokeEventMethod(event, listenerSet, false);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        eventSender.dispatchEvent(event, false);
    }
}


