package org.kasource.kaevent.config;


import java.math.BigInteger;
import java.util.ArrayList;
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
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;

/**
 * Builds the XML Configuration programmatically.
 * 
 * Create an instance and invoke method by method chaining and end with the
 * build method.
 * <p/>
 * <p/>
 * KaEventConfig config = new KaEventConfigBuilder().scan("org.abc.my").maxPoolSize(1).build()
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
    private Class<? extends ThreadPoolQueueExecutor> queueClass = ThreadPoolQueueExecutor.class;
    private Byte maxPoolSize;
    private Byte corePoolSize;
    private Long keepAliveTime;
    private Set<ChannelReg> channelRegs = new HashSet<ChannelReg>();
    private Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
    private Map<Class<? extends EventObject>, KaEventConfig.Events.Event> eventMap = 
        new HashMap<Class<? extends EventObject>, KaEventConfig.Events.Event>();

    
    /**
     * Builds and returns the configuration object.
     * 
     * @return The configuration.
     **/
    public KaEventConfig build() {
        KaEventConfig xmlConfig = new KaEventConfig();
        buildScanClassPath(xmlConfig);
        buildBeanResolver(xmlConfig);
        buildChannelFactory(xmlConfig);
        if (!events.isEmpty()) {
            buildEvents(xmlConfig);
        }
        buildQueueClass(xmlConfig);
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
     * Sets the queue class in the configuration.
     * 
     * @param xmlConfig
     *            The configuration
     **/
    private void buildQueueClass(KaEventConfig xmlConfig) {
        if (queueClass != ThreadPoolQueueExecutor.class) {
            xmlConfig.queueThread = new KaEventConfig.QueueThread();
            xmlConfig.queueThread.clazz = queueClass.getName();
        } else {
            configureQueue(xmlConfig);
        }
    }
    
    /**
     * Configure the default event queue implementation (thread pool executor).
     * 
     * @param xmlConfig the configuration
     **/
    private void configureQueue(KaEventConfig xmlConfig) {
        if (maxPoolSize != null || corePoolSize != null || keepAliveTime != null) {
            xmlConfig.threadPoolExecutor = new KaEventConfig.ThreadPoolExecutor();
            configureMaxPoolSize(xmlConfig);
            configureCorePoolSize(xmlConfig);
            configureKeepAlive(xmlConfig);
        }
    }
    
    /**
     * Configure the max pool size for the event queue.
     * 
     * @param xmlConfig the configuration.
     **/
    private void configureMaxPoolSize(KaEventConfig xmlConfig) {
        if (maxPoolSize != null) {
            xmlConfig.threadPoolExecutor.maximumPoolSize = maxPoolSize;
        }
    }

    /**
     * Configure the core pool size for the event queue.
     * 
     * @param xmlConfig the configuration.
     **/
    private void configureCorePoolSize(KaEventConfig xmlConfig) {
        if (corePoolSize != null) {
            xmlConfig.threadPoolExecutor.corePoolSize = corePoolSize;
        }
    }
    
    /**
     * Configure the keep alive time for the event queue.
     * @param xmlConfig the configuration.
     */
    private void configureKeepAlive(KaEventConfig xmlConfig) {
        if (keepAliveTime != null) {
            xmlConfig.threadPoolExecutor.keepAliveTime = new BigInteger(keepAliveTime.toString());
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
        for (Class<? extends EventObject> eventClass : events) {
            if (!eventMap.containsKey(eventClass)) {
                buildEvent(xmlConfig, eventClass);
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
                    buildEvent(xmlConfig, eventClass);
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
    private void buildEvent(KaEventConfig xmlConfig, Class<? extends EventObject> eventClass) {
        KaEventConfig.Events.Event event = new KaEventConfig.Events.Event();
        event.eventClass = eventClass.getName();
        Event eventAnnotation = eventClass.getAnnotation(Event.class);
        event.listenerInterface = eventAnnotation.listener().getName();
        event.name = eventClass.getName();
        xmlConfig.events.event.add(event);
        eventMap.put(eventClass, event);
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
     * Sets the queue (worker thread) implementation to use. Overrides the default implementation.
     * 
     * @param queue
     *            Event Queue Thread implementation class to use.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder queueClass(Class<? extends ThreadPoolQueueExecutor> queue) {
        this.queueClass = queue;
        return this;
    }

    /**
     * Set the maximum pool size on the default Queue Thread implementation, set this to 1 to get a pure serial event
     * execution thread. Note this value will not have any effect if the default Queue Thread is overridden by
     * queueClass().
     * 
     * @param queueMaxPoolSize
     *            The maximum number of threads to use by the event queue.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder maxPoolSize(byte queueMaxPoolSize) {
        this.maxPoolSize = queueMaxPoolSize;
        return this;
    }

    /**
     * Set the core pool size on the default Queue Thread implementation. Note this value will not have any effect if
     * the default Queue Thread is overridden by queueClass().
     * 
     * @param queueCorePoolSize
     *            The number of threads to initialize the event queue worker pool with.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder corePoolSize(byte queueCorePoolSize) {
        this.corePoolSize = queueCorePoolSize;
        return this;
    }

    /**
     * Set the keep alive time on the default Queue Thread implementation. Note this value will not have any effect if
     * the default Queue Thread is overridden by queueClass().
     * 
     * @param queueKeepAliveTime
     *            Keep alive time in milliseconds, idle threads of the event queue will be stopped after this timeout.
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder keepAliveTime(long queueKeepAliveTime) {
        this.keepAliveTime = queueKeepAliveTime;
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
     *            Events this channel should handle
     * 
     * @return the builder
     **/
    public KaEventConfigBuilder addChannel(String channelName, Class<? extends Channel> channelClass,
                Class<? extends EventObject>... eventsToChannel) {
        for (Class<? extends EventObject> eventClass : eventsToChannel) {
            if (!eventClass.isAnnotationPresent(Event.class)) {
                throw new IllegalArgumentException("Only events annotated with @Event can used in addChannel "
                            + eventClass + " is not annotated with @Event");
            }
        }
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

        if (!eventClass.isAnnotationPresent(Event.class)) {
            throw new IllegalArgumentException("Only events annotated with @Event can used in addEvent " + eventClass
                        + " is not annotated with @Event");
        }
        events.add(eventClass);

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
}
