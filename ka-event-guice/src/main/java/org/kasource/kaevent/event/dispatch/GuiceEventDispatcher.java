package org.kasource.kaevent.event.dispatch;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.GuiceKaEventConfigurer;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GuiceEventDispatcher extends DefaultEventDispatcher {

	
	 
	
	/**
     * Used when configured with Guice
     * 
     * @param channelRegister
     * @param channelFactory
     * @param sourceObjectListenerRegister
     * @param eventQueue
     * @param eventRouter
     */
	@Inject
    private GuiceEventDispatcher(ChannelRegister channelRegister, 
    							  ChannelFactory channelFactory,
    							  SourceObjectListenerRegister sourceObjectListenerRegister, 
    							  DispatcherQueueThread eventQueue,
    							  EventRouter eventRouter,
    							  GuiceKaEventConfigurer configurer) {
        this.channelFactory = channelFactory;
        this.channelRegister = channelRegister;
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
        this.eventQueue = eventQueue;
        this.eventRouter = eventRouter;
        configurer.configure();
    }
	
	
	
	
}
