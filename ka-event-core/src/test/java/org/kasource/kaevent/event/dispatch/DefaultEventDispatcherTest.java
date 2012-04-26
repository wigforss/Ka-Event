package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.filter.EventFilterExecutor;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.annotation.Dummy;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class DefaultEventDispatcherTest {
    
    @InjectIntoByType
    @Mock
    private EventRouter eventRouter;
    
   
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
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private EventFilterExecutor filterExecutor; 
    
    @Mock
    private ListenerChannel channel;
    
    @Mock
    private EventConfig eventConfig;
    
    @Mock
    private KaEventConfiguration config;
    
    @Dummy
    private EventObject event;
    
    @InjectIntoByType
    @Dummy
    private List<EventFilter<? extends EventObject>> bridgeFilters;
    
    
    @TestedObject
    private DefaultEventDispatcher dispatcher;
    
    @Test
    public void fireTest() {
        ChangeEvent event = new ChangeEvent("test");
        expect(eventRegister.getEventByClass(event.getClass())).andReturn(eventConfig);
        expect(eventConfig.getEventQueue()).andReturn(null);
        eventQueue.enqueue(event);
        expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.fire(event);
    }
    
    @Test
    public void fireBlocked() {
        ChangeEvent event = new ChangeEvent("test");
        eventRouter.routeEvent(event, true);
        expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.fireBlocked(event);
    }
    
    @Test(expected = IllegalStateException.class)
    public void fireOnCommitTest() {
        ChangeEvent event = new ChangeEvent("test");
        dispatcher.fireOnCommit(event);
    }

    
    @Test
    public void createChannelTest() {
        expect(channelFactory.createChannel("testChannel")).andReturn(channel);
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
        expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.registerListener(listener, source);
    }
    
    @Test
    public void doInitializeTest() {
        expect(config.getChannelRegister()).andReturn(channelRegister);
        expect(config.getSourceObjectListenerRegister()).andReturn(sourceObjectListenerRegister);
        expect(config.getDefaultEventQueue()).andReturn(eventQueue);
        expect(config.getEventRouter()).andReturn(eventRouter);
        expect(config.getChannelFactory()).andReturn(channelFactory);
        expect(config.getEventRegister()).andReturn(eventRegister);
        EasyMockUnitils.replay();
        dispatcher.doInitialize(config);
    }
    
    @Test
    public void bridgeEventPassTest() {
        expect(filterExecutor.passFilters(bridgeFilters, event)).andReturn(true);
        expect(eventRegister.getEventByClass(event.getClass())).andReturn(eventConfig);
        expect(eventConfig.getEventQueue()).andReturn(null);
        eventQueue.enqueue(event);
        expectLastCall();
        EasyMockUnitils.replay();
        dispatcher.bridgeEvent(event);
    }
    
    @Test
    public void bridgeEventNotPassTest() {
        expect(filterExecutor.passFilters(bridgeFilters, event)).andReturn(false);
        EasyMockUnitils.replay();
        dispatcher.bridgeEvent(event);
    }
}
