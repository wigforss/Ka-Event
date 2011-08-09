/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Resource;

import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.spring.transaction.TransactionListener;
import org.kasource.spring.transaction.TransactionResult;
import org.kasource.spring.transaction.TransactionSupport;
import org.kasource.spring.transaction.TransactionSupportImpl;

/**
 * @author Rikard Wigforss
 * 
 */

public class SpringEventDispatcher extends DefaultEventDispatcher implements TransactionListener {

	private ThreadLocal<Queue<EventObject>> commitEventQueue = new ThreadLocal<Queue<EventObject>>();
	

	private TransactionSupport txSupport = new TransactionSupportImpl();
    
    
    /**
     * Used when configured in XML
     * 
     * @param channelRegister
     * @param channelFactory
     * @param sourceObjectListenerRegister
     * @param eventQueue
     * @param eventRouter
     */
    private SpringEventDispatcher(ChannelRegister channelRegister, 
    							  ChannelFactory channelFactory,
    							  SourceObjectListenerRegister sourceObjectListenerRegister, 
    							  DispatcherQueueThread eventQueue,
    							  EventRouter eventRouter) {
        this.channelFactory = channelFactory;
        this.channelRegister = channelRegister;
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
        this.eventQueue = eventQueue;
        this.eventRouter = eventRouter;
    }

   

  

    @Override
    public void fireOnCommit(EventObject event) {
    	txSupport.addListener(this);
		if(commitEventQueue.get() == null) {
			commitEventQueue.set(new LinkedList<EventObject>());
		}
		commitEventQueue.get().add(event);

    }

   

    

	@Override
	public void afterCommit() {
		if(commitEventQueue.get() != null) {
			while(!commitEventQueue.get().isEmpty()) {
				EventObject event = commitEventQueue.get().poll();
				eventRouter.routeEvent(event, false);
			}
		}
		
	}

	@Override
	public void afterCompletion(TransactionResult status) {
		if(commitEventQueue.get() != null) {
			commitEventQueue.get().clear();
		}
		
	}

	@Override
	public void beforeCommit(boolean readOnly) {	
	}

	@Override
	public void beforeCompletion() {	
	}

	

}
