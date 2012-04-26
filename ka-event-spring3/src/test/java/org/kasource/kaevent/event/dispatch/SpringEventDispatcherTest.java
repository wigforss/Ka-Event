package org.kasource.kaevent.event.dispatch;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.Queue;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.ForwardedSpringApplicationEvent;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.spring.transaction.TransactionResult;
import org.kasource.spring.transaction.TransactionSupport;
import org.springframework.context.ApplicationEvent;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringEventDispatcherTest {

    @InjectInto(property = "commitEventQueue")
    @Mock
    private ThreadLocal<Queue<EventObject>> commitEventQueue;

    @InjectIntoByType
    @Mock
    private TransactionSupport txSupport;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
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
    private EventRouter eventRouter;
    
    @Mock
    private Queue<EventObject> commitQueue;
    
    @Mock
    private EventObject event;
    
    @Mock
    private ApplicationEvent springEvent;
    
    @TestedObject
    private SpringEventDispatcher eventDispatcher = new SpringEventDispatcher(channelRegister, 
                 channelFactory,
                 sourceObjectListenerRegister, 
                 eventQueue,
                 eventRouter,
                 eventRegister);
    
    @SuppressWarnings("unchecked")
    @Test
    public void fireOnCommitTest() {
        Queue<EventObject> q = new LinkedList<EventObject>();
        txSupport.addListener(eventDispatcher);
        expectLastCall();
        expect(commitEventQueue.get()).andReturn(null);
        commitEventQueue.set(isA(Queue.class));
        expectLastCall();
        expect(commitEventQueue.get()).andReturn(q);
        EasyMockUnitils.replay();
        eventDispatcher.fireOnCommit(event);
        assertFalse(q.isEmpty());
        assertEquals(event, q.poll());
    }
    
    @Test
    public void afterCommitTest() {
        Queue<EventObject> q = new LinkedList<EventObject>();
        q.add(event);
        expect(commitEventQueue.get()).andReturn(q).times(4);
        eventRouter.routeEvent(event, false);
        expectLastCall();
        EasyMockUnitils.replay();
        eventDispatcher.afterCommit();
    }
    
    @Test
    public void afterCompletionTest() {
        expect(commitEventQueue.get()).andReturn(commitQueue).times(2);
        commitQueue.clear();
        expectLastCall();
        EasyMockUnitils.replay();
        eventDispatcher.afterCompletion(TransactionResult.STATUS_COMMITTED);
    }
    
    @Test
    public void onApplicationEventTest() {
        Capture<ForwardedSpringApplicationEvent> forwardedEvent = new Capture<ForwardedSpringApplicationEvent>();
        expect(eventRegister.hasEventByClass(springEvent.getClass())).andReturn(true);
        eventQueue.enqueue(capture(forwardedEvent));
        expectLastCall();
        EasyMockUnitils.replay();
        eventDispatcher.onApplicationEvent(springEvent);
        assertEquals(springEvent, forwardedEvent.getValue().getSource());
    }
}
