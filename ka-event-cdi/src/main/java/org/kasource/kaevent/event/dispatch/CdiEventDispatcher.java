package org.kasource.kaevent.event.dispatch;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.CdiKaEventConfigurer;
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
	
	public CdiEventDispatcher() { 
		
	}
	
	
	@PostConstruct
	public void initialize() {
	    setChannelFactory(channelFactory);
        setChannelRegister(channelRegister);
        setSourceObjectListenerRegister(sourceObjectListenerRegister);
        setEventQueue(eventQueue);
        setEventRouter(eventRouter);
		configurer.configure();
	}
	

	
}
