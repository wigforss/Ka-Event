package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.KaEventConfig;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventConfigurer;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * The default implementation of EventDispatcher.
 * 
 * @author wigforss
 * @version $Id$
 */
public class DefaultEventDispatcher implements EventDispatcher, KaEventInitializedListener {

	private final ThreadLocal<LinkedList<EventObject>> batchListByThread = 
		new ThreadLocal<LinkedList<EventObject>>();
	
    protected EventRouter eventRouter;
    protected ChannelRegister channelRegister;
    protected ChannelFactory channelFactory;
    protected SourceObjectListenerRegister sourceObjectListenerRegister;
    protected DispatcherQueueThread eventQueue;
    
    

	private KaEventConfigurer configurer = new KaEventConfigurer();
    
    /**
     * Create an Event Dispatcher configured by file found af <i>configLocation</i>.
     * 
     * @param configLocation	Location of configuration XML file. 
     * <i>classpath:</i> and <i>file:</i> prefixes are supported, default is classpath.
     **/
    public DefaultEventDispatcher(String configLocation) {
        initialize(configLocation);
    }
    
    /**
     * Create an Event Dispatcher configured by the configuration object, 
     * see org.kasource.kaevent.config.KaEventConfigBuilder to build
     * a configuration object programatically.
     *  
     * @param config	The configuration object
     **/
    public DefaultEventDispatcher(KaEventConfig config) {
        initialize(config);
    }
    
    
   
    
    protected DefaultEventDispatcher() {
       
    }
   
    
   
    
    
    protected void initialize(String configLocation) {
    	KaEventInitializer.getInstance().addListener(this);
    	configurer.configure(this, configLocation);
    }
    
    protected void initialize(KaEventConfig config) {
    	KaEventInitializer.getInstance().addListener(this);
    	configurer.configure(this, config);
    }
    
    @Override
    public void doInitialize(KaEventConfiguration config) {
        this.channelRegister = config.getChannelRegister();
        this.sourceObjectListenerRegister = config.getSourceObjectListenerRegister();
        this.eventQueue = config.getQueueThread();
        this.eventRouter = config.getEventRouter();
        this.channelFactory = config.getChannelFactory();
    }
   
    
    @Override
    public void fire(EventObject event) {
        eventQueue.enqueue(event);        
    }

   
    @Override
    public void fireBlocked(EventObject event) {
        eventRouter.routeEvent(event, true);       
    }

  
    @Override
    public void fireOnCommit(EventObject event) {
        throw new IllegalStateException("Not implemented in " + this.getClass());
        
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
    
    @Override
    public void registerListenerAtChannel(EventListener listener,
            String channelName) {
    	Channel channel = channelRegister.getChannel(channelName);
    	if (channel instanceof ListenerChannel) {
    		((ListenerChannel) channel).registerListener(listener);
    	} else {
    		throw new IllegalArgumentException("Channel " + channelName + " is not an ListenerChannel.");
    	}
    }
    
    @Override
    public void registerListenerAtChannel(EventListener listener,
            String channelName, List<EventFilter<EventObject>> filters) {
    	Channel channel = channelRegister.getChannel(channelName);
    	if (channel instanceof ListenerChannel) {
    		((ListenerChannel) channel).registerListener(listener, filters);
    	} else {
    		throw new IllegalArgumentException("Channel " + channelName + " is not an ListenerChannel.");
    	}
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
             eventRouter.routeEvent(batchList.removeFirst(), false);       
            }
        }
    }
}
