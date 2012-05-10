package org.kasource.kaevent.example.spring.configure.channel;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class ExampleConfiguration {

    @Bean(name = "springChannel")
    @Autowired
    public Channel provideSpringChannel(ChannelFactory channelFactory) {
        Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
        events.add(ContextRefreshedEvent.class);
        return channelFactory.createChannel(ChannelImpl.class, "springChannel", events);
    }
}
