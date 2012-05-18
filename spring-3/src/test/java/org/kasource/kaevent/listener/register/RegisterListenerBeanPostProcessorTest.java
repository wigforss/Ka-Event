package org.kasource.kaevent.listener.register;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class RegisterListenerBeanPostProcessorTest {
    
    @TestedObject
    private RegisterListenerBeanPostProcessor beanPostProcessor;
    
    @InjectIntoByType
    @Mock
    private RegisterListenerByAnnotation register;
    
    @Test
    public void registerBeanListenerTest() {
        Capture<BeanListener> beanAnnotation = new Capture<BeanListener>();
        Capture<ChannelListener> channelAnnotation = new Capture<ChannelListener>();
        PersonService service = new PersonService();
        register.registerBeanListener(capture(beanAnnotation), eq(service));
        expectLastCall();
        register.registerChannelListener(capture(channelAnnotation), eq(service));
        expectLastCall();
        EasyMockUnitils.replay();
        beanPostProcessor.postProcessAfterInitialization(service, "beanName");
        assertEquals("personFactory", beanAnnotation.getValue().value()[0]);
        assertEquals("personChannel", channelAnnotation.getValue().value()[0]);
    }
    
    @BeanListener("personFactory")
    @ChannelListener("personChannel")
    private static class PersonService {
        
    }
}
