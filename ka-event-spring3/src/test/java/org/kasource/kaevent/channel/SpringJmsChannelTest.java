package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.jms.core.JmsTemplate;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.annotation.Dummy;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringJmsChannelTest {
    
    @InjectIntoByType
    @Mock
    private ChannelRegister channelRegister; 
    
    @InjectIntoByType
    @Mock
    private EventRegister eventRegister;
    
    @InjectIntoByType
    @Mock
    private ChannelFilterHandler filterHandler;
    
    @InjectIntoByType
    @Mock
    private JmsTemplate jmsTemplate;
    
    @Dummy
    private EventObject event;
 
    @Dummy
    private EventFilter<EventObject> filter;
    
    @TestedObject
    private SpringJmsChannel channel = new SpringJmsChannel(channelRegister, eventRegister);
    
    @Test
    public void unregisterEventTest() {
        channelRegister.unregisterEventHandler(channel, EventObject.class);
        expectLastCall();
        filterHandler.unregisterFilterFor(EventObject.class);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.unregisterEvent(EventObject.class);
    }
    
    @Test
    public void sendTest() {
        expect(filterHandler.filterEvent(event)).andReturn(true);
        jmsTemplate.convertAndSend(event);
        expectLastCall();
        EasyMockUnitils.replay();
        channel.fireEvent(event, true);
    }
    
    @Test
    public void registerFilterTest() {
        expect(filterHandler.registerFilter(filter)).andReturn(true);   
        EasyMockUnitils.replay();
        channel.registerFilter(filter);
     
    }
    
   
}
