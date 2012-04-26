package org.kasource.kaevent.config;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.config.KaEventConfig.Channels;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.method.MethodResolver;

/**
 * Builds the XML Configuration programmatically.
 * 
 * Create an instance and invoke method by method chaining and end with the
 * build method.
 * <p/>
 * <p/>
 * KaEventConfig config = new KaEventConfigBuilder().scan("org.abc.my").defaultEventQueueMaxThreads(1).build()
 * <p/>
 * Which will create a new configuration the scans for @Event annotated classes under org.abc.my and
 * set the event queue max size to 1 (allowing only one thread making it strictly sequential).
 * 
 * @author Rikard Wigforss
 * @version $Id: $
 **/
public class KaEventConfigBuilder {
    
    
    private String scanClassPath;
    private Class<? extends BeanResolver> beanResolverClass;
    private Class<? extends ChannelFactory> channelFactoryClass;
    private EventQueueReg defaultEventQueue = null;
    private Map<String, EventQueueReg> eventQueues = new HashMap<String, KaEventConfigBuilder.EventQueueReg>();
    private Set<ChannelReg> channelRegs = new HashSet<ChannelReg>();
    private Set<EventReg> events = new HashSet<EventReg>();
    private Map<Class<? extends EventObject>, KaEventConfig.Events.Event> eventMap = 
            new HashMap<Class<? extends EventObject>, KaEventConfig.Events.Event>();

    
    /**
     * Builds and returns the configuration object.
     * 
     * @return The configuration.
     **/
    public KaEventConfig build() {
        KaEventConfig xmlConfig = new KaEventConfig();
        buildDefaultEventQueue(xmlConfig);
        buildEventQueues(xmlConfig);
        buildScanClassPath(xmlConfig);
        buildBeanResolver(xmlConfig);
        buildChannelFactory(xmlConfig);
        if (!events.isEmpty()) {
            buildEvents(xmlConfig);
        }
       
        if (!channelRegs.isEmpty()) {
            buildChannels(xmlConfig);
        }
        return xmlConfig;
    }

    /**
     * Builds the scan class path.
     * 
     * @param xmlConfig the configuration.
     **/
    private void buildScanClassPath(KaEventConfig xmlConfig) {
        if (scanClassPath != null && scanClassPath.trim().length() > 0) {
            xmlConfig.events = new KaEventConfig.Events();
            xmlConfig.events.scanClassPath = scanClassPath.trim();
        }
    }
    
    /**
     * Builds the bean resolver.
     * 
     * @param xmlConfig the configuration.
     **/
    private void buildBeanResolver(KaEventConfig xmlConfig) {
        if (beanResolverClass != null) {
            xmlConfig.beanResolver = new KaEventConfig.BeanResolver();
            xmlConfig.beanResolver.clazz = beanResolverClass.getName();
        }
    }
    
    /**
     * Builds the channel factory.
     * 
     * @param xmlConfig the configuration.
     **/
    private void buildChannelFactory(KaEventConfig xmlConfig) {
        if (channelFactoryClass != null) {
            xmlConfig.channelFactory = new KaEventConfig.ChannelFactory();
            xmlConfig.channelFactory.clazz = channelFactoryClass.getName();
        }
    }
    
    /**
     *  Builds the default Event Queue
     * 
     * @param xmlConfig
     *            The configuration
     **/
    private void buildDefaultEventQueue(KaEventConfig xmlConfig) {
        if(defaultEventQueue != null) {
            xmlConfig.getEventQueue().add(defaultEventQueue.toXmlObject());
        }
    }
    
   /**
    * Builds additional event queues.
    * 
    * @param xmlConfig The configuration
    */
    private void buildEventQueues(KaEventConfig xmlConfig) {
        for(EventQueueReg eventQueueReg : eventQueues.values()) {
            xmlConfig.getEventQueue().add(eventQueueReg.toXmlObject());
        }
    }
    
    
    /**
     * Builds the channels and register the events.
     * 
     * @param xmlConfig
     *            The configuration
     **/
    private void buildChannels(KaEventConfig xmlConfig) {
        buildEventsForChannels(xmlConfig);
        xmlConfig.channels = new KaEventConfig.Channels();
        xmlConfig.channels.channel = new ArrayList<Channels.Channel>();
        for (ChannelReg channelReg : channelRegs) {
            KaEventConfig.Channels.Channel channel = new KaEventConfig.Channels.Channel();
            channel.name = channelReg.getChannelName();
            channel.setClassName(channelReg.getChannelClass().getName());
            channel.handle = new ArrayList<Channels.Channel.Handle>();
            for (Class<? extends EventObject> eventClass : channelReg.getEvents()) {
                KaEventConfig.Channels.Channel.Handle handle = new KaEventConfig.Channels.Channel.Handle();
                handle.event = eventMap.get(eventClass).getName();
                channel.handle.add(handle);
            }

            xmlConfig.channels.channel.add(channel);
        }

    }

    /**
     * Build all events from the events set into the configuration.
     * 
     * @param xmlConfig The configuration.
     **/
    private void buildEvents(KaEventConfig xmlConfig) {
        if (xmlConfig.events == null) {
            xmlConfig.events = new KaEventConfig.Events();
        }
        if (xmlConfig.events.event == null) {
            xmlConfig.events.event = new ArrayList<KaEventConfig.Events.Event>();
        }
        for (EventReg event : events) {
            if (!eventMap.containsKey(event)) {
                buildEvent(xmlConfig, event);
            }
        }
    }

    /**
     * Build all events from a channel registration to the configuration and add them to the eventMap.
     * 
     * @param xmlConfig The configuration.
     **/
    private void buildEventsForChannels(KaEventConfig xmlConfig) {

        if (xmlConfig.events == null) {
            xmlConfig.events = new KaEventConfig.Events();
        }
        if (xmlConfig.events.event == null) {
            xmlConfig.events.event = new ArrayList<KaEventConfig.Events.Event>();
        }

        buildEventsForRegistredChannel(xmlConfig);

    }

    /**
     * Build all events from a channel registration to the configuration and add them to the eventMap.
     * 
     * @param xmlConfig The configuration.
     **/
    private void buildEventsForRegistredChannel(KaEventConfig xmlConfig) {
        for (ChannelReg channelReg : channelRegs) {
            for (Class<? extends EventObject> eventClass : channelReg.getEvents()) {
                if (!eventMap.containsKey(eventClass)) {
                    buildEvent(xmlConfig, new EventReg(eventClass));
                }
            }
        }
    }
    
    /**
     * Build an Event into the eventMap.
     * 
     * @param xmlConfig The configuration.
     * @param eventClass Event to build configuration for.
     **/
    private void buildEvent(KaEventConfig xmlConfig, EventReg eventReg) {
        KaEventConfig.Events.Event event = new KaEventConfig.Events.Event();
        event.eventClass = eventReg.getEventClass().getName();
        Event eventAnnotation = eventReg.getEventClass().getAnnotation(Event.class);
        if(eventAnnotation == null) {
            if(eventReg.getInterfaceClass() != null) {
                event.listenerInterface = eventReg.getInterfaceClass().getName();
            } 
            if(eventReg.getAnnotationClass() != null) {
                event.annotation = eventReg.getAnnotationClass().getName();
            }
        }
        String eventQueue = eventReg.getEventQueueName();
        if(eventQueue != null) {
            if (!eventQueues.containsKey(eventQueue) && !Event.DEFAULT_EVENT_QUEUE_NAME.equals(eventQueue)) {
                throw new IllegalStateException("Event Queue " + eventQueue + " set for event " + eventReg.getEventClass() + " could not be found!");
            }
            event.eventQueue = eventQueue;
        }
        event.name = eventReg.getEventClass().getName();
        xmlConfig.events.event.add(event);
        eventMap.put(eventReg.getEventClass(), event);
    }

    /**
     * Scan the supplied class path for event classes annotated with @Event.
     * 
     * @param scanClassPathPackage
     *            class path (package and sub-packages) to scan for events.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder scan(String scanClassPathPackage) {
        this.scanClassPath = scanClassPathPackage;
        return this;
    }

    /**
     * Set the BeanResolver implementation to use.
     * 
     * @param beanResolver
     *            BeanResolver implementation to use.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder beanResolver(Class<? extends BeanResolver> beanResolver) {
        this.beanResolverClass = beanResolver;
        return this;
    }

    /**
     * Set the ChannelFactory implementation to use.
     * 
     * @param channelFactory
     *            ChannelFactory implementation to use.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder channelFactory(Class<? extends ChannelFactory> channelFactory) {
        this.channelFactoryClass = channelFactory;
        return this;
    }

    /**
     * Optionally sets the event queue implementation to use for the default event queue. 
     * Overrides the default implementation.
     * 
     * @param queue
     *            Event Queue Thread implementation class to use.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder defaultEventQueueClass(Class<? extends DispatcherQueueThread> queue) {
        getDefaultEventQueue().setQueueClass(queue);
        return this;
    }

    private EventQueueReg getDefaultEventQueue() {
        if(defaultEventQueue == null) {
            defaultEventQueue = new EventQueueReg(Event.DEFAULT_EVENT_QUEUE_NAME);;
        }
        return defaultEventQueue;
    }
    
    /**
     * Set the maximum threads on the default Event Queue, set this to 1 to get  pure serial event
     * event execution. 
     * 
     * @param maxThreads
     *            The maximum number of threads to use by the event queue.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder defaultEventQueueMaxThreads(short maxThreads) {
        getDefaultEventQueue().setMaxThreads(maxThreads);
        return this;
    }
    
    /**
     * Configure threads of the default Event Queue, set maxThreads to 1 to get pure serial event
     * event execution. 
     * 
     * @param maxThreads
     *            The maximum number of threads to use by the event queue.
     * @param coreThreads
     *            The number of threads to start with.
     * @param keepAliveTime
     *            Keep alive time in milliseconds, idle threads of the event queue will be stopped after this timeout.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder defaultEventQueueThreads(byte maxThreads, byte coreThreads, long keepAliveTime) {
        getDefaultEventQueue().setMaxThreads(maxThreads);
        getDefaultEventQueue().setCoreThreads(coreThreads);
        getDefaultEventQueue().setKeepAliveTime(keepAliveTime);
        return this;
    }

    /**
     * Adds a new named EventQueue.
     * 
     * @param name Name of the new event queue.
     * 
     * @return the builder.
     **/
    public KaEventConfigBuilder addEventQueue(String name) {
        EventQueueReg eventQueue = new EventQueueReg(name);
        eventQueues.put(name, eventQueue);
        return this;
    }
    
    
    /**
     * Adds a new named EventQueue.
     * 
     * @param name Name of the new event queue.
     * 
     * @return the builder.
     **/
    public KaEventConfigBuilder addEventQueue(String name, int maxThreads) {
        EventQueueReg eventQueue = new EventQueueReg(name);
        eventQueue.setMaxThreads((short) maxThreads);
        eventQueues.put(name, eventQueue);
        return this;
    }
    
    /**
     * Adds a new named EventQueue using the supplied eventQueue class
     * 
     * @param name              Name of the event queue
     * @param eventQueueClass   Class of the event queue
     * 
     * @return the builder.
     **/
    public KaEventConfigBuilder addEventQueue(String name, Class<? extends DispatcherQueueThread> eventQueueClass) {
        EventQueueReg eventQueue = new EventQueueReg(name);
        eventQueue.setQueueClass(eventQueueClass);
        eventQueues.put(name, eventQueue);
        return this;
    }
    
    /**
     * Adds a new named EventQueue using the supplied eventQueue class
     * 
     * @param name              Name of the event queue
     * @param eventQueueClass   Class of the event queue
     * 
     * @return the builder.
     **/
    public KaEventConfigBuilder addEventQueue(String name, int maxThreads, Class<? extends DispatcherQueueThread> eventQueueClass) {
        EventQueueReg eventQueue = new EventQueueReg(name);
        eventQueue.setQueueClass(eventQueueClass);
        eventQueue.setMaxThreads((short) maxThreads);
        eventQueues.put(name, eventQueue);
        return this;
    }
    
    
    
    /**
     * Set thread configuration on a previously added event queue.
     * 
     * @param name
     *            Name of the event queue to modify.
     * @param maxThreads
     *            The maximum number of threads to use by the event queue.
     * @param coreThreads
     *            The number of threads to start with.
     * @param keepAliveTime
     *            Keep alive time in milliseconds, idle threads of the event queue will be stopped after this timeout.
     * @return the builder.
     * @throws IllegalArgumentException if no event queue has been added with name.
     */
    public KaEventConfigBuilder eventQueueThreads(String name, short maxThreads, short coreThreads, long keepAliveTime) throws IllegalArgumentException{
        EventQueueReg eventQueue = eventQueues.get(name);
        if(eventQueue == null) {
            throw new IllegalArgumentException("No eventQueue with name: " + name + " has been added!");
        }
        eventQueue.setMaxThreads(maxThreads);
        eventQueue.setCoreThreads(coreThreads);
        eventQueue.setKeepAliveTime(keepAliveTime);
        return this;
    }
   
    /**
     * Add a channel to the configuration.
     * 
     * @param channelName       Name of the channel.
     * @param eventsToChannel   Event to register at the channel.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder addChannel(String channelName, Class<? extends EventObject>... eventsToChannel) {
        return addChannel(channelName, ChannelImpl.class, eventsToChannel);
    }

    /**
     * Add a channel to the configuration.
     * 
     * @param channelName
     *            Name of the channel
     * @param channelClass
     *            The Channel Implementation class to use.            
     * @param eventsToChannel
     *            Events this channel should handle, if not added  with addEvent, addEventByInterface or addEventByAnnotation these 
     *            event classes  must be annotated with @Event
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder addChannel(String channelName, Class<? extends Channel> channelClass,
                Class<? extends EventObject>... eventsToChannel) {
        channelRegs.add(new ChannelReg(channelName, channelClass, eventsToChannel));
        return this;
    }

    /**
     * Add an event to the configuration.
     * 
     * @param eventClass
     *            The class name of the event to add, must be annotated with @Event.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder addEvent(Class<? extends EventObject> eventClass) {
        Event eventAnnotation = eventClass.getAnnotation(Event.class);
        if (eventAnnotation == null) {
            throw new IllegalArgumentException("Only events annotated with @Event can used in addEvent " + eventClass
                        + " is not annotated with @Event");
        } else if(eventAnnotation.listener().equals(EventListener.class) && eventAnnotation.annotation().equals(Event.class)){
            throw new IllegalArgumentException("@Event attribute listener and / or annotation must be set for " + eventClass
                        + " to bind event to an interaface and / or an annotation.");
        }
        events.add(new EventReg(eventClass));

        return this;
    }
    
    /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class name of the event to add, must be annotated with @Event.
    * @param listenerInterfaceClass
    *            The Listener Interface to associate the eventClass with.
    * 
    * @return the builder
    **/
   public KaEventConfigBuilder addEventBoundToInterface(Class<? extends EventObject> eventClass, Class<? extends EventListener> listenerInterfaceClass) {

       events.add(new EventReg(eventClass, listenerInterfaceClass, null));

       return this;
   }
   
   /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class name of the event to add, must be annotated with @Event.
    * @param listenerInterfaceClass
    *            The Listener Interface to associate the eventClass with.
    * @param eventQueue
    *            Name of the event queue to handle this event.
    * 
    * @return the builder
    **/
   public KaEventConfigBuilder addEventBoundToInterface(Class<? extends EventObject> eventClass, Class<? extends EventListener> listenerInterfaceClass, String eventQueue) {

       EventReg event = new EventReg(eventClass, listenerInterfaceClass, null);
       event.setEventQueueName(eventQueue);
       events.add(event);

       return this;
   }
   
   /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class of the event to add, should not be annotated with @Event.
    * @param listenerInterfaceClass
    *            The Listener Interface to associate the eventClass with, may be null if eventMethodAnnotation is set.
    * @param eventMethodAnnotation
    *            The listener method annotation to associate the event with, must have retention RUNTIME, may be null if listenerInterfaceClass is set.
    * 
    * @return the builder
    **/
   public KaEventConfigBuilder addEvent(Class<? extends EventObject> eventClass, 
                                        Class<? extends EventListener> listenerInterfaceClass, 
                                        Class<? extends Annotation> eventMethodAnnotation) {

       
       events.add(new EventReg(eventClass, listenerInterfaceClass, eventMethodAnnotation));

       return this;
   }
   
   /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class of the event to add, should not be annotated with @Event.
    * @param listenerInterfaceClass
    *            The Listener Interface to associate the eventClass with, may be null if eventMethodAnnotation is set.
    * @param eventMethodAnnotation
    *            The listener method annotation to associate the event with, must have retention RUNTIME, may be null if listenerInterfaceClass is set.
    * @param eventQueue
    *            Name of the event queue to handle this event. 
    * @return the builder
    **/
   public KaEventConfigBuilder addEvent(Class<? extends EventObject> eventClass, 
                                        Class<? extends EventListener> listenerInterfaceClass, 
                                        Class<? extends Annotation> eventMethodAnnotation,
                                        String eventQueue) {

       EventReg event = new EventReg(eventClass, listenerInterfaceClass, eventMethodAnnotation);
       event.setEventQueueName(eventQueue);
       events.add(event);

       return this;
   }
   
   
   
   /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class name of the event to add, should not be annotated with @Event.
    * @param eventMethodAnnotation
    *            The listener method annotation to associate the event with, must have retention RUNTIME.
    * 
    * @return the builder
    **/
   public KaEventConfigBuilder addEventBoundToAnnotation(Class<? extends EventObject> eventClass, Class<? extends Annotation> eventMethodAnnotation) {

       events.add(new EventReg(eventClass, null, eventMethodAnnotation));

       return this;
   }
   
   /**
    * Add an event to the configuration.
    * 
    * @param eventClass
    *            The class name of the event to add, should not be annotated with @Event.
    * @param eventMethodAnnotation
    *            The listener method annotation to associate the event with, must have retention RUNTIME.
    * @param eventQueue
    *            Name of the event queue to handle this event.
    * @return the builder
    **/
   public KaEventConfigBuilder addEventBoundToAnnotation(Class<? extends EventObject> eventClass, Class<? extends Annotation> eventMethodAnnotation, String eventQueue) {

       EventReg event = new EventReg(eventClass, null, eventMethodAnnotation);
       event.setEventQueueName(eventQueue);
       events.add(event);

       return this;
   }
    
  
    

    /**
     * Channel Registration.
     * 
     * @author rikardwi
     **/
    private static class ChannelReg {
        private String channelName;
        private Class<? extends Channel> channelClass;
        private Class<? extends EventObject>[] events;

        /**
         * Constructor.
         * 
         * @param channelName   Name of channel.
         * @param channelClass  Implementation class.
         * @param events        Events to register.
         **/
        public ChannelReg(String channelName, Class<? extends Channel> channelClass,
                    Class<? extends EventObject>[] events) {

            this.channelName = channelName;
            this.channelClass = channelClass;
            this.events = events;
        }

        /**
         * Returns the name of the channel.
         * 
         * @return name of the channel.
         **/
        public String getChannelName() {
            return channelName;
        }

        /**
         * Returns the events handled by this channel.
         * 
         * @return The events handled by this channel.
         **/
        public Class<? extends EventObject>[] getEvents() {
            return events;
        }

        /**
         * Returns the implementation class.
         * 
         * @return the implementation class.
         **/
        public Class<? extends Channel> getChannelClass() {
            return channelClass;
        }

    }
    
    private static class EventReg {
        private Class<? extends EventObject> eventClass;
        private Class<? extends EventListener> interfaceClass;
        private Class<? extends Annotation> annotationClass;
        private String eventQueueName;
        
        public EventReg(Class<? extends EventObject> eventClass) {
            this.eventClass = eventClass;
        }
        
        public EventReg(Class<? extends EventObject> eventClass, 
                        Class<? extends EventListener> interfaceClass,
                        Class<? extends Annotation> annotationClass) {
            this(eventClass);
            this.interfaceClass = interfaceClass;
            this.annotationClass = annotationClass;
        }

        /**
         * @return the eventClass
         */
        public Class<? extends EventObject> getEventClass() {
            return eventClass;
        }

       

        /**
         * @return the interfaceClass
         */
        public Class<? extends EventListener> getInterfaceClass() {
            return interfaceClass;
        }

      

        /**
         * @return the annotationClass
         */
        public Class<? extends Annotation> getAnnotationClass() {
            return annotationClass;
        }

      
        /**
         * @return the eventQueueName
         */
        public String getEventQueueName() {
            return eventQueueName;
        }

        /**
         * @param eventQueueName the eventQueueName to set
         */
        public void setEventQueueName(String eventQueueName) {
            this.eventQueueName = eventQueueName;
        }
    }
    
    private static class EventQueueReg {
        private String name;
        private Class<? extends DispatcherQueueThread> queueClass = ThreadPoolQueueExecutor.class;
        private short maxThreads;
        private short coreThreads;
        private long keepAliveTime;
        
        public EventQueueReg(String name) {
            this.name = name;
        }

       

        

        /**
         * @param queueClass the queueClass to set
         */
        public void setQueueClass(Class<? extends DispatcherQueueThread> queueClass) {
            this.queueClass = queueClass;
        }

       

        /**
         * @param maxThreads to set
         */
        public void setMaxThreads(short maxThreads) {
            this.maxThreads = maxThreads;
        }

      
        /**
         * @param coreThreads to set
         */
        public void setCoreThreads(short coreThreads) {
            this.coreThreads = coreThreads;
        }

        
        /**
         * @param keepAliveTime the keepAliveTime to set
         */
        public void setKeepAliveTime(Long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }
        
        public KaEventConfig.EventQueue toXmlObject() {
            KaEventConfig.EventQueue eventQueue = new KaEventConfig.EventQueue();
            eventQueue.setName(name);
            eventQueue.setClazz(queueClass.getName());
            if (maxThreads > 0) {
                eventQueue.setMaxThreads(maxThreads);
            }
            if (coreThreads > 0) {
                eventQueue.setCoreThreads(coreThreads);
            }
            if (keepAliveTime > 0) {
                eventQueue.setKeepAliveTime(keepAliveTime);
            }
            return eventQueue;
        }
        
    }
}
