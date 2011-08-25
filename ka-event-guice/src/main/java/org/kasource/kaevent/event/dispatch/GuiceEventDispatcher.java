package org.kasource.kaevent.event.dispatch;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.GuiceKaEventConfigurer;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Guice Event Dispatcher.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceEventDispatcher extends DefaultEventDispatcher {

	 
	
	/**
     * Used when configured with Guice.
     * 
     * @param channelRegister Channel Register
     * @param channelFactory  Channel Factory
     * @param sourceObjectListenerRegister Source Object Listener Register
     * @param eventQueue    Event Queue
     * @param eventRouter   Event Router
     * @param configurer    Configurer for the Guice environment.
     */
	@Inject
    GuiceEventDispatcher(ChannelRegister channelRegister, 
    							  ChannelFactory channelFactory,
    							  SourceObjectListenerRegister sourceObjectListenerRegister, 
    							  DispatcherQueueThread eventQueue,
    							  EventRouter eventRouter,
    							  GuiceKaEventConfigurer configurer) {
        setChannelFactory(channelFactory);
        setChannelRegister(channelRegister);
        setSourceObjectListenerRegister(sourceObjectListenerRegister);
        setEventQueue(eventQueue);
        setEventRouter(eventRouter);
        configurer.configure();
    }
	
	
	
	
}
