package org.kasource.kaevent.event.dispatch;

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
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.filter.EventFilterExecutor;
import org.kasource.kaevent.event.register.EventRegister;
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
	
	private EventRouter eventRouter;
    private ChannelRegister channelRegister;
    private ChannelFactory channelFactory;
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    private DispatcherQueueThread eventQueue;
    private EventRegister eventRegister;
    private List<EventFilter<? extends EventObject>> bridgeFilters = new LinkedList<EventFilter<? extends EventObject>>();
    private EventFilterExecutor filterExecutor = new EventFilterExecutor();
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
    
    
   
    /**
     * Protected Constructor.
     * 
     * This Constructor will not configure the Ka-Event environment.
     **/
    protected DefaultEventDispatcher() {      
    }
   
    
   
    
    /**
     * Configures the Ka-Event environment from a configuration location.
     * 
     * @param configLocation    location of the configuration XML file. Use the classpath: or file: prefix:
     **/
    protected void initialize(String configLocation) {
    	KaEventInitializer.getInstance().addListener(this);
    	configurer.configure(this, configLocation);   
    }
    
    /**
     * Configures the Ka-Event environment from configuration object.
     * 
     * @param config    The configuration object, use KaEventConfigBuilder to create the configuration object.
     **/
    protected void initialize(KaEventConfig config) {
    	KaEventInitializer.getInstance().addListener(this);
    	configurer.configure(this, config);
    }
    
    @Override
    public void doInitialize(KaEventConfiguration config) {
        this.channelRegister = config.getChannelRegister();
        this.sourceObjectListenerRegister = config.getSourceObjectListenerRegister();
        this.eventQueue = config.getDefaultEventQueue();
        this.eventRouter = config.getEventRouter();
        this.channelFactory = config.getChannelFactory();
        this.eventRegister = config.getEventRegister();
    }
   
    
    @Override
    public void fire(EventObject event) throws IllegalArgumentException {
        EventConfig config = eventRegister.getEventByClass(event.getClass());
        if(config == null) {
            throw new IllegalArgumentException(event.getClass() + " is not a registerd event.");
        }
        if(config.getEventQueue() != null) {
            config.getEventQueue().enqueue(event);       
        } else {
            eventQueue.enqueue(event); 
        }
    }

   
    public void bridgeEvent(EventObject event) {
        if(eventRegister.hasEventByClass(event.getClass()) 
                    && filterExecutor.passFilters(bridgeFilters, event)){
                fire(event);        
        }
    }
    
    @Override
    public void fireBlocked(EventObject event) {
        validateEvent(event);
        eventRouter.routeEvent(event, true);       
       
    }
    
    /**
     * Validates that the event is registered.
     * 
     * @param event Event to inspect
     * 
     * @throws IllegalArgumentException if the event is not registered.
     **/
    protected void validateEvent(EventObject event) throws IllegalArgumentException{
        if(!eventRegister.hasEventByClass(event.getClass())) {
            throw new IllegalArgumentException(event.getClass() + " is not a registerd event.");   
        }
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
    public void registerListener(Object listener,
            Object sourceObject) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
    }

    
    @Override
    public void registerListener(Object listener, Object sourceObject, List<EventFilter<? extends EventObject>> filters) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
        
    }
    
    @Override
    public void registerListenerAtChannel(Object listener,
            String channelName) {
    	Channel channel = channelRegister.getChannel(channelName);
    	if (channel instanceof ListenerChannel) {
    		((ListenerChannel) channel).registerListener(listener);
    	} else {
    		throw new IllegalArgumentException("Channel " + channelName + " is not an ListenerChannel.");
    	}
    }
    
    @Override
    public void registerListenerAtChannel(Object listener,
            String channelName, List<EventFilter<? extends EventObject>> filters) {
    	Channel channel = channelRegister.getChannel(channelName);
    	if (channel instanceof ListenerChannel) {
    		((ListenerChannel) channel).registerListener(listener, filters);
    	} else {
    		throw new IllegalArgumentException("Channel " + channelName + " is not an ListenerChannel.");
    	}
    }

   /**
    * Add event to this threads batch.
    * 
    * @param event Event object.
    **/
    @Override
    public void addToBatch(EventObject event) {
        validateEvent(event);
        LinkedList<EventObject> batchList = batchListByThread.get();
        if (batchList == null) {
            batchList = new LinkedList<EventObject>();
            batchListByThread.set(batchList);
        }
        batchList.add(event);
    }
    
    /**
     * Clear this threads batch. 
     **/
    @Override
    public void clearBatch() {
        LinkedList<EventObject> batchList = batchListByThread.get();
        if (batchList != null) {
            batchList.clear();
        }
    }

    /**
     * Fire all events in this threads batch.
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

    /**
     * @return the eventRouter
     */
    protected EventRouter getEventRouter() {
        return eventRouter;
    }

    /**
     * @param eventRouter the eventRouter to set
     */
    protected void setEventRouter(EventRouter eventRouter) {
        this.eventRouter = eventRouter;
    }

    /**
     * @return the channelRegister
     */
    protected ChannelRegister getChannelRegister() {
        return channelRegister;
    }

    /**
     * @param channelRegister the channelRegister to set
     */
    protected void setChannelRegister(ChannelRegister channelRegister) {
        this.channelRegister = channelRegister;
    }

    /**
     * @return the channelFactory
     */
    protected ChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    protected void setChannelFactory(ChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    /**
     * @return the sourceObjectListenerRegister
     */
    protected SourceObjectListenerRegister getSourceObjectListenerRegister() {
        return sourceObjectListenerRegister;
    }

    /**
     * @param sourceObjectListenerRegister the sourceObjectListenerRegister to set
     */
    protected void setSourceObjectListenerRegister(SourceObjectListenerRegister sourceObjectListenerRegister) {
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
    }

    /**
     * @return the eventQueue
     */
    protected DispatcherQueueThread getEventQueue() {
        return eventQueue;
    }

    /**
     * @param eventQueue the eventQueue to set
     */
    protected void setEventQueue(DispatcherQueueThread eventQueue) {
        this.eventQueue = eventQueue;
    }

    /**
     * @return the configurer
     */
    protected KaEventConfigurer getConfigurer() {
        return configurer;
    }

    /**
     * @param configurer the configurer to set
     */
    protected void setConfigurer(KaEventConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * @return the eventRegister
     */
    protected EventRegister getEventRegister() {
        return eventRegister;
    }

    /**
     * @param eventRegister the eventRegister to set
     */
    protected void setEventRegister(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }
    
    @Override
    public void unregisterListener(Object listener, Object sourceObject) {
       sourceObjectListenerRegister.unregisterListener(listener, sourceObject);
        
    }

    @Override
    public void unregisterListenerFromChannel(Object listener, String channelName)
                throws IllegalArgumentException {
        Channel channel = channelRegister.getChannel(channelName);
        if(channel instanceof ListenerChannel) {
            ListenerChannel listenerChannel = (ListenerChannel) channel;
            listenerChannel.unregisterListener(listener);
        }
        
    }

    @Override
    public <T extends EventObject> void addBridgeFilter(EventFilter<T> filter) {
       bridgeFilters.add(filter);
        
    }

    
}
