package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.CdiKaEventConfigurer;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

@ApplicationScoped @Named("kaEvent.eventDispatcher")
public class CdiEventDispatcher extends DefaultEventDispatcher {

    @Inject
    private CdiKaEventConfigurer configurer;
    
    @Inject
    private ChannelFactory channelFactory;
    
    @Inject
    private ChannelRegister channelRegister;
    
    @Inject
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    @Inject
    private DispatcherQueueThread eventQueue;
    
    @Inject
    private EventRouter eventRouter;
    
    @Inject
    private EventRegister eventRegister;
    
    public CdiEventDispatcher() { 
        
    }
    
    
    @PostConstruct
    public void initialize() {
        setChannelFactory(channelFactory);
        setChannelRegister(channelRegister);
        setSourceObjectListenerRegister(sourceObjectListenerRegister);
        setEventQueue(eventQueue);
        setEventRouter(eventRouter);
        setEventRegister(eventRegister);
        configurer.configure();
    }
    
    public void eventListener(@Observes EventObject event) {
        bridgeEvent(event);
    }

    
}
