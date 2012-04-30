package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.Queue;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.filter.AlreadySeenEventFilter;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.spring.transaction.TransactionListener;
import org.kasource.spring.transaction.TransactionResult;
import org.kasource.spring.transaction.TransactionSupport;
import org.kasource.spring.transaction.TransactionSupportImpl;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Spring Event Dispatcher.
 * 
 * Fires Spring Application Events.
 * 
 * Adds support for fireOnCommit.
 * 
 * @author Rikard Wigforss
 */
public final class SpringEventDispatcher extends DefaultEventDispatcher implements TransactionListener, ApplicationListener<ApplicationEvent> {

	private ThreadLocal<Queue<EventObject>> commitEventQueue = new ThreadLocal<Queue<EventObject>>();
	

	private TransactionSupport txSupport = new TransactionSupportImpl();
    
	private EventFilter<? extends EventObject> bridgeFilter = new AlreadySeenEventFilter();
    
    /**
     * Used when configured in XML.
     * 
     * @param channelRegister Channel Register.
     * @param channelFactory    Channel Factory
     * @param sourceObjectListenerRegister Source Object Listener Register.
     * @param eventQueue    Event Queue.
     * @param eventRouter   Event Router.
     */
     SpringEventDispatcher(ChannelRegister channelRegister, 
    							  ChannelFactory channelFactory,
    							  SourceObjectListenerRegister sourceObjectListenerRegister, 
    							  DispatcherQueueThread eventQueue,
    							  EventRouter eventRouter,
    							  EventRegister eventRegister) {
        setChannelFactory(channelFactory);
        setChannelRegister(channelRegister);
        setSourceObjectListenerRegister(sourceObjectListenerRegister);
        setEventQueue(eventQueue);
        setEventRouter(eventRouter);
        setEventRegister(eventRegister);
        addBridgeFilter(bridgeFilter);
       
    }

   

  

    @Override
    public void fireOnCommit(EventObject event) {
    	txSupport.addListener(this);
		if (commitEventQueue.get() == null) {
			commitEventQueue.set(new LinkedList<EventObject>());
		}
		commitEventQueue.get().add(event);

    }

   

    

	@Override
	public void afterCommit() {
		if (commitEventQueue.get() != null) {
			while (!commitEventQueue.get().isEmpty()) {
				EventObject event = commitEventQueue.get().poll();
				getEventRouter().routeEvent(event, false);
			}
		}
		
	}

	@Override
	public void afterCompletion(TransactionResult status) {
		if (commitEventQueue.get() != null) {
			commitEventQueue.get().clear();
		}
		
	}

	@Override
	public void beforeCommit(boolean readOnly) {	
	}

	@Override
	public void beforeCompletion() {	
	}





    @Override
    public void onApplicationEvent(ApplicationEvent springEvent) {
        
        if(getEventRegister().hasEventByClass(springEvent.getClass())) {
            bridgeEvent(springEvent);
        }
        
    }

	

}
