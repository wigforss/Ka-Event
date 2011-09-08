package org.kasource.kaevent.example.cdi.simple;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.kasource.kaevent.cdi.EventScanPackage;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeEvent;

@ApplicationScoped
public class ExampleConfiguration {

    @Produces @EventScanPackage 
    String packageToScanForEvents = ExampleConfiguration.class.getPackage().getName();
    
    @Inject
    private CommandConsole commandConsole;
    
    /*
    @Produces @Named("temperatureChannel")
    Channel getTempratureChannel(ChannelFactory channelFactory) {
        return channelFactory.createChannel("temperatureChannel");
    }
    */
    
    /*
    @Produces @Named("temperatureChannel")
    Channel getTempratureChannel(ChannelFactory channelFactory) {
        Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
        events.add(TemperatureChangeEvent.class);
        return channelFactory.createChannel(ChannelImpl.class, "temperatureChannel", events);
    }
    *
    
    /*
    @Produces @Named("tempEvent")
    EventConfig getTempratureEvent(EventFactory eventFactory) {
        return eventFactory.newFromAnnotatedEventClass(TemperatureChangeEvent.class, "tempEvent");
    }
    */
}
