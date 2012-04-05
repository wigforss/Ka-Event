package org.kasource.kaevent.channel;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ChannelFactoryBeanTest {
    
    @TestedObject
    private ChannelFactoryBean factory;
    
    @InjectIntoByType
    @Mock
    private ApplicationContext applicationContext;
    
    @Mock
    private ChannelFactory channelFactory;
    
    @Mock
    private EventRegister eventRegister;
    
    @Mock
    private ChannelRegister channelRegister;
    
    @Mock
    private Channel channel;
    
    @Test
    public void createChannelTest() throws Exception {
        String channelName = "channelName";
        Class<? extends Channel> channelClass = ChannelImpl.class;
        factory.setName(channelName);
        factory.setChannelClass(channelClass);
        
        expect(applicationContext.getBean(KaEventSpringBean.CHANNEL_FACTORY.getId())).andReturn(channelFactory);
        expect(applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId())).andReturn(eventRegister);
        expect(applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId())).andReturn(channelRegister);
        expect(channelFactory.createChannel(channelClass, channelName)).andReturn(channel);
        expect(applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId())).andReturn(channelRegister);
        channelRegister.registerChannel(channel);
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(channel, factory.getObject());
        
    }
}
