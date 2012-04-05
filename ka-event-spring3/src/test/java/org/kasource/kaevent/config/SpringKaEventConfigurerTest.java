package org.kasource.kaevent.config;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.config.EventBuilder;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringKaEventConfigurerTest {
    
   
    
    @InjectIntoByType
    private String scanClassPath = "scanPath";
    
    @InjectIntoByType
    @Mock
    private KaEventConfiguration configuration;
    
    @InjectIntoByType
    @Mock
    private ApplicationContext applicationContext;
   
    @Mock
    private EventBuilderFactory eventBuilderFactory;
    
    @Mock
    private EventRegister eventRegister;
    
    @Mock
    private EventBuilder builder;
    
    @Mock
    private EventConfig event;
    
    @Mock
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    @Mock
    private ChannelRegister channelRegister;
    
    @Mock
    private ChannelFactory channelFactory;
    
    @Mock
    private BeanResolver beanResolver;
    
    @TestedObject
    private SpringKaEventConfigurer configurer = new SpringKaEventConfigurer(configuration);
    
    @Test
    public void minimalConfigurationTest() {
        expect(configuration.getEventBuilderFactory()).andReturn(eventBuilderFactory);
        expect(configuration.getEventRegister()).andReturn(eventRegister);
        expect(applicationContext.getBeansOfType(EventConfig.class)).andReturn(null);
        expect(configuration.getEventBuilderFactory()).andReturn(eventBuilderFactory);
        expect(configuration.getEventRegister()).andReturn(eventRegister);
        for(SpringEvent springEvent : SpringEvent.values()) {
            expect(eventBuilderFactory.getBuilder(springEvent.getEvent())).andReturn(builder);
            expect(builder.bindInterface(springEvent.getListener(), springEvent.getListenerMethod())).andReturn(builder);
            expect(builder.bindAnnotation(springEvent.getMethodAnnotation())).andReturn(builder);
            expect(builder.build()).andReturn(event);
            eventRegister.registerEvent(event);
            expectLastCall();
        }
        expect(applicationContext.getBeansOfType(Channel.class)).andReturn(null);
        expect(configuration.getSourceObjectListenerRegister()).andReturn(sourceObjectListenerRegister);
        expect(configuration.getEventRegister()).andReturn(eventRegister); 
        expect(configuration.getChannelFactory()).andReturn(channelFactory); 
        expect(configuration.getChannelRegister()).andReturn(channelRegister);
        expect(eventRegister.getEventClasses()).andReturn(Collections.EMPTY_SET);
  /*      expect(configuration.getChannelRegister()).andReturn(channelRegister);
        expect(configuration.getSourceObjectListenerRegister()).andReturn(sourceObjectListenerRegister); 
        expect(configuration.getBeanResolver()).andReturn(beanResolver);*/
        EasyMockUnitils.replay();
        configurer.configure();
    }
}
