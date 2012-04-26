package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.ForwardedSpringApplicationEvent;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextStoppedEvent;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.annotation.Dummy;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringEventChannelTest {

    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister;
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    @InjectIntoByType
    @Mock
    private ChannelFilterHandler filterHandler;
    
    @TestedObject
    private SpringEventChannel channel = new SpringEventChannel(channelRegister, eventRegister);
    
    @Mock
    private ApplicationEvent event;
    
    @Dummy
    private ApplicationContext applicationContext;
    
    @Test
    public void test() {
        expect(filterHandler.filterEvent(event)).andReturn(true);
        eventPublisher.publishEvent(event);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.fireEvent(event, true);
    }
    
    
    
    @Test
    public void acceptForwardedEventFalse() {
        ForwardedSpringApplicationEvent event = new ForwardedSpringApplicationEvent(new ContextStoppedEvent(applicationContext)); 
        assertFalse(channel.acceptForwardedEvent(event)); 
       
    }
}
