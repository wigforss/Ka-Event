package org.kasource.kaevent.config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelFactoryImpl;
import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.FilterableChannel;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.EventFactoryImpl;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.export.EventExporter;
import org.kasource.kaevent.event.export.XmlConfigEventExporter;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

/**
 * Class that performs the actual configuration of the ka-event environment.
 * 
 * @author wigforss
 * @version $Id$
 **/
public class KaEventConfigurer {
    private static final Logger LOG = Logger.getLogger(KaEventConfigurer.class);

    private KaEventConfigurationImpl configuration = null;
    private ConfigurationXmlFileLoader xmlLoader = new ConfigurationXmlFileLoader();
    
    
    /**
     * Configure the environment based on the configuration provided.
     * 
     * @param eventDispatcher
     *            The event dispatcher.
     * @param xmlConfig
     *            Configuration to use.
     **/
    public void configure(EventDispatcher eventDispatcher, KaEventConfig xmlConfig) {
        if (xmlConfig != null) {
            configuration = configureByXml(xmlConfig);
        } else {
            configuration = defaultConfiguration();
        }

        configuration.setEventDispatcher(eventDispatcher);
        KaEventInitializer.setConfiguration(configuration);
    }

    /**
     * Configure the environment based on a XML Configuration file.
     * 
     * The configLocation can be read from both classpath and file, use a prefix to indicate which kind of location to
     * use. Supported prefixes are classpath: and file:
     * 
     * If configLocation is null the default location will be tried <i>classpath:kaevent-config.xml</i>.
     * 
     * @param eventDispatcher
     *            The event dispatcher.
     * @param configLocation
     *            Location of the XML file.
     * @throws IllegalArgumentException if the configuration could not be read.
     **/
    public void configure(EventDispatcher eventDispatcher, String configLocation) throws IllegalArgumentException {

        KaEventConfig xmlConfig = null;
        if (configLocation != null) {
            xmlConfig = xmlLoader.loadXmlFromPath(configLocation);
        } else {
            // Try default location       
            xmlConfig = xmlLoader.loadXmlFromPath("classpath:kaevent-config.xml");
            
        }
        configure(eventDispatcher, xmlConfig);

    }

    /**
     * Scan the classpath for classes annated with @Event and register the events found.
     * 
     * The scanClassPath parameter should be a package name on dot notation. All sub-packages to the supplied package
     * will also be scanned. You can also provide a comma separated list of packages to scan.
     * 
     * @param scanClassPath
     *            package(s) to scan
     **/
    public void scanClassPathForEvents(String scanClassPath) {
        if (scanClassPath != null && scanClassPath.length() > 0) {
            importAndRegisterEvents(new AnnotationEventExporter(scanClassPath), configuration.getEventFactory(),
                        configuration.getEventRegister());
        }
    }

    /**
     * Creates and returns the default configuration.
     * 
     * @return The result of configuring the default.
     **/
    private KaEventConfigurationImpl defaultConfiguration() {
        KaEventConfigurationImpl config = new KaEventConfigurationImpl();
        // Bean resolver
        config.setBeanResolver(new DefaultBeanResolver());
        // Events
        config.setEventFactory(new EventFactoryImpl(config.getBeanResolver()));
        config.setEventRegister(new DefaultEventRegister(config.getEventFactory()));

        // Channel Register
        config.setChannelRegister(new ChannelRegisterImpl());

        // Source Object Listener Register
        config.setSourceObjectListenerRegister(new SourceObjectListenerRegisterImpl(config.getEventRegister(), config
                    .getBeanResolver()));
        config.setEventMethodInvoker(new EventMethodInvokerImpl(config.getEventRegister()));

        config.setEventRouter(new EventRouterImpl(config.getChannelRegister(),
                    config.getSourceObjectListenerRegister(), config.getEventMethodInvoker()));

        // Channel Factory
        config.setChannelFactory(new ChannelFactoryImpl(config.getChannelRegister(), config.getEventRegister(), config
                    .getEventMethodInvoker(), config.getBeanResolver()));

        config.setQueueThread(new ThreadPoolQueueExecutor(config.getEventRouter()));

        return config;

    }

    /**
     * Configure the ka-event environment by a configuration object and return the result.
     * 
     * @param xmlConfig
     *            Configuration to use.
     * 
     * @return The result of applying the xmlConfig.
     **/
    private KaEventConfigurationImpl configureByXml(KaEventConfig xmlConfig) {
        KaEventConfigurationImpl config = new KaEventConfigurationImpl();
        // Bean Resolver
        BeanResolver beanResolver = null;
        KaEventConfig.BeanResolver beanResolverConfig = xmlConfig.getBeanResolver();
        if (beanResolverConfig != null && beanResolverConfig.getClazz() != null) {
            config.setBeanResolver(ReflectionUtils.getInstance(beanResolverConfig.getClazz(), BeanResolver.class));
        } else {
            config.setBeanResolver(new DefaultBeanResolver());
        }

        // Events
        config.setEventFactory(new EventFactoryImpl(config.getBeanResolver()));
        config.setEventRegister(new DefaultEventRegister(config.getEventFactory()));
        config.setEventMethodInvoker(new EventMethodInvokerImpl(config.getEventRegister()));
        config.setSourceObjectListenerRegister(new SourceObjectListenerRegisterImpl(config.getEventRegister(), config
                    .getBeanResolver()));

        // Channel Register
        config.setChannelRegister(new ChannelRegisterImpl());
        // Sender
        config.setEventRouter(new EventRouterImpl(config.getChannelRegister(),
                    config.getSourceObjectListenerRegister(), config.getEventMethodInvoker()));

        // Channel Factory
        if (xmlConfig.getChannelFactory() != null && xmlConfig.getChannelFactory().getClazz() != null) {
            ChannelFactory channelFactory = createChannelFactory(xmlConfig.getChannelFactory().getClazz(),
                        config.getChannelRegister(), config.getEventRegister(), config.getEventMethodInvoker(),
                        config.getBeanResolver());
            config.setChannelFactory(channelFactory);
        } else {
            config.setChannelFactory(new ChannelFactoryImpl(config.getChannelRegister(), config.getEventRegister(),
                        config.getEventMethodInvoker(), config.getBeanResolver()));
        }
        // Queue Thread
        configureEventQueue(xmlConfig, config);
        // import events
        importEvents(xmlConfig, config, beanResolver);
        createChannels(xmlConfig, config);
        registerEventsAtChannels(config);
        return config;

    }

    /**
     * Import events from XML Configuration.
     * 
     * @param xmlConfig     XML Configuration.
     * @param config        Configuration result.
     * @param beanResolver  Bean Resolver.
     **/
    private void importEvents(KaEventConfig xmlConfig, KaEventConfigurationImpl config, BeanResolver beanResolver) {
        KaEventConfig.Events events = xmlConfig.getEvents();
        if (events != null) {
            String scanPath = events.getScanClassPath();
            if (scanPath != null && scanPath.length() > 0) {
                importAndRegisterEvents(new AnnotationEventExporter(scanPath), config.getEventFactory(),
                            config.getEventRegister());
            }
            importAndRegisterEvents(new XmlConfigEventExporter(events.getEvent(), beanResolver),
                        config.getEventFactory(), config.getEventRegister());
        }
    }

    /**
     * Configure the Event Queue.
     * 
     * @param xmlConfig XML Configuration.
     * @param config    Configuration result.
     **/
    private void configureEventQueue(KaEventConfig xmlConfig, KaEventConfigurationImpl config) {
        if (xmlConfig.getQueueThread() == null) {
            config.setQueueThread(new ThreadPoolQueueExecutor(config.getEventRouter()));
            ThreadPoolQueueExecutor threadPoolExecutor = new ThreadPoolQueueExecutor(config.getEventRouter());
            configureDefaultEventQueue(xmlConfig, threadPoolExecutor);
            config.setQueueThread(threadPoolExecutor);
        } else {
            try {
                config.setQueueThread(ReflectionUtils.getInstance(xmlConfig.getQueueThread().getClazz(),
                            DispatcherQueueThread.class, new Class<?>[] { EventRouter.class },
                            new Object[] { config.getEventRouter() }));
            } catch (IllegalStateException ise) {
                config.setQueueThread(ReflectionUtils.getInstance(xmlConfig.getQueueThread().getClazz(),
                            DispatcherQueueThread.class));
            }

        }
    }

    /**
     * Configure the default Event Queue Implementation (ThreadPoolExecutor).
     * 
     * @param xmlConfig           XML Configuration.
     * @param threadPoolExecutor  Configuration result.
     **/
    private void configureDefaultEventQueue(KaEventConfig xmlConfig, ThreadPoolQueueExecutor threadPoolExecutor) {
        if (xmlConfig.getThreadPoolExecutor() != null) {
            if (xmlConfig.getThreadPoolExecutor().getMaximumPoolSize() != null) {
                threadPoolExecutor.setMaximumPoolSize(xmlConfig.getThreadPoolExecutor().getMaximumPoolSize());
            }
            if (xmlConfig.getThreadPoolExecutor().getCorePoolSize() != null) {
                threadPoolExecutor.setCorePoolSize(xmlConfig.getThreadPoolExecutor().getCorePoolSize());
            }
            if (xmlConfig.getThreadPoolExecutor().getKeepAliveTime() != null) {
                threadPoolExecutor.setKeepAliveTime(xmlConfig.getThreadPoolExecutor().getKeepAliveTime()
                            .longValue(), TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Create the channel factory from clazz.
     * 
     * @param clazz              Channel implementation class to instanceiate.
     * @param channelRegister    Channel register.
     * @param eventRegister      Event Register.
     * @param eventMethodInvoker Event method invoker.
     * @param beanResolver       Bean resolver.
     * 
     * @return A new ChannelFactory.
     */
    protected ChannelFactory createChannelFactory(String clazz, ChannelRegister channelRegister,
                EventRegister eventRegister, EventMethodInvoker eventMethodInvoker, BeanResolver beanResolver) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends ChannelFactory> channelFactoryClass = (Class<? extends ChannelFactory>) Class
                        .forName(clazz);
            Constructor<? extends ChannelFactory> cons = channelFactoryClass.getConstructor(ChannelRegister.class,
                        EventRegister.class, EventMethodInvoker.class, BeanResolver.class);
            return cons.newInstance(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create ChannelFactory from " + clazz, e);
        }
    }

    /**
     * Register @Event annotated events with the channels referenced in the channels attribute.
     * 
     * @param config The configuration build this far.
     */
    protected void registerEventsAtChannels(KaEventConfiguration config) {
        EventRegister eventRegister = config.getEventRegister();
        ChannelFactory channelFactory = config.getChannelFactory();
        ChannelRegister channelRegister = config.getChannelRegister();
        for (Class<? extends EventObject> eventClass : eventRegister.getEventClasses()) {
            Event eventAnno = eventClass.getAnnotation(Event.class);
            if (eventAnno != null && eventAnno.channels().length > 0) {
                registerEventAtChannelByAnnotation(channelFactory, channelRegister, eventClass, eventAnno);
            }
        }
    }

    /**
     * Registers Events at Channels by the @Event annotation.
     * 
     * @param channelFactory   Channel Factory.
     * @param channelRegister  Channel Register.
     * @param eventClass       Event Class.
     * @param eventAnno        Event annotation.
     **/
    private void registerEventAtChannelByAnnotation(ChannelFactory channelFactory, ChannelRegister channelRegister,
                Class<? extends EventObject> eventClass, Event eventAnno) {
        
            String[] channelsByEvent = eventAnno.channels();
            for (String channelName : channelsByEvent) {
                Channel channel = null;
                try {
                    channel = channelRegister.getChannel(channelName);
                    channel.registerEvent(eventClass);
                } catch (NoSuchChannelException nsce) {
                    if (eventAnno.createChannels()) {
                        channel = channelFactory.createChannel(channelName);
                    } else {
                        throw new NoSuchChannelException(eventClass.getName()
                                    + " @Event annotation referenced to a channel named " + channelName
                                    + " which does not exist!");
                    }
                }
                channel.registerEvent(eventClass);
            }
        
    }

    /**
     * Create the channel configured in the xmlConfig parameters.
     * 
     * @param xmlConfig
     *            The configuration to use.
     * @param config
     *            Use this to lookup the environment configured this far.
     **/
    private void createChannels(KaEventConfig xmlConfig, KaEventConfiguration config) {
        EventRegister eventRegister = config.getEventRegister();
        ChannelFactory channelFactory = config.getChannelFactory();
        // Create channels
        KaEventConfig.Channels channels = xmlConfig.getChannels();
        if (channels != null) {
            for (KaEventConfig.Channels.Channel channel : channels.getChannel()) {
                String name = channel.getName();
                Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
                registerEventsForChannel(eventRegister, channel, eventSet);
                Class<? extends Channel> channelClass = getChannelClass(channel);
                createChannelInstance(config, channelFactory, channel, name, eventSet, channelClass);
            }
        }
    }

    /**
     * Creates a channel instance .
     * 
     * @param config            Configuration result.
     * @param channelFactory    Channel Factory to use  when creating the channel.
     * @param channel           Channel XML Configuration Element.
     * @param name              Name of the channel.
     * @param eventSet          Set of events to register at the channel.
     * @param channelClass      Channel class to create instance for.
     */
    private void createChannelInstance(KaEventConfiguration config, ChannelFactory channelFactory,
                KaEventConfig.Channels.Channel channel, String name, 
                Set<Class<? extends EventObject>> eventSet,
                Class<? extends Channel> channelClass) {
        Channel newChannel = null;
        if (eventSet.isEmpty()) {

            newChannel = channelFactory.createChannel(channelClass, name);
        } else {
            newChannel = channelFactory.createChannel(channelClass, name, eventSet);
        }
        setFiltersForChannel(config, channel, newChannel);
    }

    /**
     * Set filters for a channel.
     * 
     * @param config        Configuration result.
     * @param channel       Channel XML Configuration Element.
     * @param newChannel    Channel instance.
     **/
    private void setFiltersForChannel(KaEventConfiguration config, KaEventConfig.Channels.Channel channel,
                Channel newChannel) {
        if (newChannel != null && newChannel instanceof FilterableChannel && channel.getFilter() != null) {
            String[] filters = channel.getFilter().split(",");

            for (String filter : filters) {
                @SuppressWarnings("unchecked")
                EventFilter<EventObject> eventFilter = config.getBeanResolver().getBean(filter.trim(),
                            EventFilter.class);
                ((FilterableChannel) newChannel).registerFilter(eventFilter);
            }

        }
    }

    /**
     * Returns channel class from a Channel XML Configuration Element.
     * 
     * @param channel Channel XML Configuration Element.
     * 
     * @return Channel class.
     **/
    @SuppressWarnings("unchecked")
    private Class<? extends Channel> getChannelClass(KaEventConfig.Channels.Channel channel) {
        Class<? extends Channel> channelClass = ChannelImpl.class;
        if (channel.getClassName() != null) {
            try {
                channelClass = (Class<? extends ListenerChannel>) Class.forName(channel.getClassName());
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalStateException(channel.getClassName() + " Could not be found ", cnfe);
            } catch (ClassCastException cce) {
                throw new IllegalStateException(channel.getClassName() + " Must implement "
                            + ListenerChannel.class, cce);
            }
        }
        return channelClass;
    }

    /**
     * Register events for a channel.
     * 
     * @param eventRegister Event Register.
     * @param channel       Channel XML Configuration Element.
     * @param eventSet      Set of events to register at the channel.
     **/
    private void registerEventsForChannel(EventRegister eventRegister, KaEventConfig.Channels.Channel channel,
                Set<Class<? extends EventObject>> eventSet) {
        if (channel.getHandle() != null) {
            for (KaEventConfig.Channels.Channel.Handle handleEvent : channel.getHandle()) {
                String eventName = handleEvent.getEvent();
                
                EventConfig eventConfig = eventRegister.getEventByName(eventName);
                if (eventConfig != null) {
                    eventSet.add(eventConfig.getEventClass());
                } else {
                    throw new IllegalStateException("Event " + eventName + " could not be found!");
                }
            }
        }
    }

    /**
     * Import and register events from the eventExporter provided.
     * 
     * @param eventExporter
     *            Creates the events to register.
     * @param eventFactory
     *            Used by the exporter.
     * @param eventRegister
     *            Event Register.
     **/
    protected void importAndRegisterEvents(EventExporter eventExporter, EventFactory eventFactory,
                EventRegister eventRegister) {
        Set<EventConfig> events;
        try {
            events = eventExporter.exportEvents(eventFactory);
            for (EventConfig event : events) {
                eventRegister.registerEvent(event);
            }
        } catch (RuntimeException re) {
            LOG.error("Error when importing annotated events", re);
            throw re;
        } catch (IOException e) {
            throw new IllegalStateException("Could not import events", e);
        }

    }

   
}
