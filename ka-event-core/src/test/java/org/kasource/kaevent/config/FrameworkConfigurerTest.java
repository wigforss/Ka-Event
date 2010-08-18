/**
 * 
 */
package org.kasource.kaevent.config;



import java.util.Properties;

import static org.easymock.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
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
public class FrameworkConfigurerTest {

    @Mock
    private DefaultEventDispatcher eventDispatcher;
    
   
    @TestedObject
    private FrameworkConfigurer configurer;
    

    
    @Test
    public void configureTest() {

        eventDispatcher.setEventSender(isA(EventSender.class));
        expectLastCall();
        eventDispatcher.setEventQueue(isA(DispatcherQueueThread.class));
        expectLastCall();
        eventDispatcher.setSourceObjectListenerRegister(isA(SourceObjectListenerRegister.class));
        expectLastCall();
        eventDispatcher.setChannelRegister(isA(ChannelRegister.class));
        expectLastCall();
        eventDispatcher.setChannelFactory(isA(ChannelFactory.class));
        expectLastCall();
        EasyMockUnitils.replay();
        configurer.configure(eventDispatcher, "classpath:org/kasource/kaevent/config/simple-config.xml");
    }
}
