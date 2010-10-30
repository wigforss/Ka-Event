/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventConfigurer;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * @author wigforss
 *
 */
public class DefaultEventDispatcher implements EventDispatcher, KaEventInitializedListener{

	private final ThreadLocal<LinkedList<EventObject>> batchListByThread = new ThreadLocal<LinkedList<EventObject>>();
	
    protected EventSender eventSender;
    protected ChannelRegister channelRegister;
    protected ChannelFactory channelFactory;
    protected SourceObjectListenerRegister sourceObjectListenerRegister;
    protected DispatcherQueueThread eventQueue;
   
    private KaEventConfigurer configurer = new KaEventConfigurer();
    
    public DefaultEventDispatcher(String scanPath) {
        initialize(scanPath);
    }
    
    public DefaultEventDispatcher() {
        initialize(null);
    }
    
    protected void initialize(String scanPath) {
    	KaEventInitializer.getInstance().addListener(this);
    	configurer.configure(this,scanPath);
    }
    
    @Override
    public void doInitialize(KaEventConfiguration config) {
        this.channelRegister = config.getChannelRegister();
        this.sourceObjectListenerRegister = config.getSourceObjectListenerRegister();
        this.eventQueue = config.getQueueThread();
        this.eventSender = config.getEventSender();
        this.channelFactory = config.getChannelFactory();
    }
   
  
    
    @Override
    public void fire(EventObject event) {
        eventQueue.enqueue(event);        
    }

   
    @Override
    public void fireBlocked(EventObject event) {
        eventSender.dispatchEvent(event, true);       
    }

  
    @Override
    public void fireOnCommit(EventObject event) {
        throw new IllegalStateException("Not implemented in "+this.getClass());
        
    }

    @Override
    public Channel createChannel(String channelName) {
        return channelFactory.createChannel(channelName);
    }
 
    @Override
    public Channel getChannel(String channelName) { 
        return channelRegister.getChannel(channelName);
    }

    @Override
    public void registerListener(EventListener listener,
            Object sourceObject) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
    }

    
    @Override
    public void registerListener(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
        
    }

	

   /**
    * Add event to this threads batch
    * 
    * @param event
    **/
    @Override
    public void addToBatch(EventObject event) {
        LinkedList<EventObject> batchList = batchListByThread.get();
        if (batchList == null) {
            batchList = new LinkedList<EventObject>();
            batchListByThread.set(batchList);
        }
        batchList.add(event);
    }
    
    /**
     * Clear this threads batch 
     **/
    @Override
    public void clearBatch() {
        LinkedList<EventObject> batchList = batchListByThread.get();
        if (batchList != null) {
            batchList.clear();
        }
    }

    /**
     * Fire all events in this threads batch
     **/
    @Override
    public void fireBatch() {
        LinkedList<EventObject> batchList = batchListByThread.get();
        if (batchList != null) {
            while (!batchList.isEmpty()) {
            	eventSender.dispatchEvent(batchList.removeFirst(), false);       
            }
        }
    }

    
    
    
    
    
   

    
    

 
    

}
