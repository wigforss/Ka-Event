/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.channel.ChannelRegister;
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
public class DefaultEventDispatcherTest {
    
    @InjectIntoByType
    @Mock
    private EventRouter eventRouter;
    
    @SuppressWarnings("unused")
    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private ChannelFactory channelFactory;
    
    @InjectIntoByType
    @Mock
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    @InjectIntoByType
    @Mock
    private DispatcherQueueThread eventQueue;
    
    @Mock
    private Channel channel;
    
    @TestedObject
    private DefaultEventDispatcher dispatcher;
    
    @Test
    public void fireTest() {
        ChangeEvent event = new ChangeEvent("test");
        eventQueue.enqueue(event);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.fire(event);
    }
    
    @Test
    public void fireBlocked() {
        ChangeEvent event = new ChangeEvent("test");
        eventRouter.dispatchEvent(event, true);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.fireBlocked(event);
    }
    
    @Test(expected=IllegalStateException.class)
    public void fireOnCommitTest() {
        ChangeEvent event = new ChangeEvent("test");
        dispatcher.fireOnCommit(event);
    }

    
    @Test
    public void createChannelTest() {
        EasyMock.expect(channelFactory.createChannel("testChannel")).andReturn(channel);
        EasyMockUnitils.replay();
        dispatcher.createChannel("testChannel"); 
    }
    
   
    @Test
    public void registerListenerTest() {
        ChangeListener listener = new ChangeListener() {
            ///CLOVER:OFF
            @Override
            public void stateChanged(ChangeEvent e) {
                
            }
            ///CLOVER:ON
        };
        Object source = new Object();
        sourceObjectListenerRegister.registerListener(listener, source);
        EasyMock.expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.registerListener(listener, source);
    }
}
