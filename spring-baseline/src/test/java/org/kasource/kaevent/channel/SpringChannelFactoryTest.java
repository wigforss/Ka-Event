package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringChannelFactoryTest {
    
    
    
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
    

    
    @TestedObject
    private SpringChannelFactory factory = new SpringChannelFactory(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
    
    @Test
    public void createDefaultChannel() {
        String channelName = "channelName";
        expect(channelRegister.getChannel(channelName)).andThrow(new NoSuchChannelException("Test"));
        channelRegister.registerChannel(isA(Channel.class));
        expectLastCall();
        EasyMockUnitils.replay();
        Channel channel = factory.createChannel(channelName);
        assertNotNull(channel);
        assertTrue(channel instanceof ChannelImpl);
        assertEquals(channelName, channel.getName());
        
    }

}
