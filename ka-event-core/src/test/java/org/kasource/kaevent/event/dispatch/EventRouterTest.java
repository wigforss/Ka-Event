/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
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
public class EventRouterTest {
    
    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    @InjectIntoByType
    @Mock
    private EventMethodInvoker invoker;
    
    @Mock
    private ListenerChannel channel;
    
    @Mock
    private Set<EventListenerRegistration> listenerSet;
    
    @TestedObject
    private EventRouterImpl eventRouter;
    
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
        eventRouter.routeEvent(event, false);
    }
}


