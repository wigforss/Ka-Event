package org.kasource.kaevent.config;

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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.config.KaEventConfig.Channels;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;

public class KaEventConfigBuilder {

	private String scanClassPath;
	private Class<? extends BeanResolver> beanResolverClass;
	private Class<? extends ThreadPoolQueueExecutor> queueClass = ThreadPoolQueueExecutor.class;
	private Byte maxPoolSize;
	private Byte corePoolSize;
	private Long keepAliveTime;
	private Set<ChannelReg> channelRegs = new HashSet<ChannelReg>();
	private Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();
	Map<Class<? extends EventObject>, KaEventConfig.Events.Event> eventMap = new HashMap<Class<? extends EventObject>, KaEventConfig.Events.Event>();
	
	
	public KaEventConfigBuilder() {
		
	}

	public KaEventConfig build() {
		KaEventConfig xmlConfig = new KaEventConfig();
		if (scanClassPath != null && scanClassPath.trim().length() > 0) {
			xmlConfig.events = new KaEventConfig.Events();
			xmlConfig.events.scanClassPath = scanClassPath.trim();
		}
		if (beanResolverClass != null) {
			xmlConfig.beanResolver = new KaEventConfig.BeanResolver();
			xmlConfig.beanResolver.clazz = beanResolverClass.getName();
		}
		if(!events.isEmpty()) {
			buildEvents(xmlConfig);
		}
		buildQueueClass(xmlConfig);
		if (!channelRegs.isEmpty()) {
			buildChannels(xmlConfig);
		}
		return xmlConfig;
	}

	/**
	 * Sets the queue class in the configuration
	 * 
	 * @param xmlConfig
	 *            The configuration
	 **/
	private void buildQueueClass(KaEventConfig xmlConfig) {
		if (queueClass != ThreadPoolQueueExecutor.class) {
			xmlConfig.queueThread = new KaEventConfig.QueueThread();
			xmlConfig.queueThread.clazz = queueClass.getName();
		} else {
			if (maxPoolSize != null || corePoolSize != null
					|| keepAliveTime != null) {
				xmlConfig.threadPoolExecutor = new KaEventConfig.ThreadPoolExecutor();
				if (maxPoolSize != null) {
					xmlConfig.threadPoolExecutor.maximumPoolSize = maxPoolSize;
				}
				if (corePoolSize != null) {
					xmlConfig.threadPoolExecutor.corePoolSize = corePoolSize;
				}
				if (keepAliveTime != null) {
					xmlConfig.threadPoolExecutor.keepAliveTime = new BigInteger(
							keepAliveTime.toString());
				}
			}
		}
	}

	/**
	 * Builds the channels and register the events
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
			channel.handle = new ArrayList<Channels.Channel.Handle>();
			for (Class<? extends EventObject> eventClass : channelReg
					.getEvents()) {
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
	 * @param xmlConfig
	 **/
	private void buildEvents(KaEventConfig xmlConfig) {
		if (xmlConfig.events == null) {
			xmlConfig.events = new KaEventConfig.Events();
		}
		if(xmlConfig.events.event == null) {
			xmlConfig.events.event = new ArrayList<KaEventConfig.Events.Event>();
		}
		for (Class<? extends EventObject> eventClass : events){
			if (!eventMap.containsKey(eventClass)) {
				buildEvent(xmlConfig, eventClass);
			}
		}
	}
	
	/**
	 * Build all events from a channel registration to the configuration and add them to the eventMap
	 * 
	 * @param xmlConfig
	 **/
	private void buildEventsForChannels(KaEventConfig xmlConfig) {
		
		if (xmlConfig.events == null) {
			xmlConfig.events = new KaEventConfig.Events();
		}
		if(xmlConfig.events.event == null) {
			xmlConfig.events.event = new ArrayList<KaEventConfig.Events.Event>();
		}

		for (ChannelReg channelReg : channelRegs) {
			for (Class<? extends EventObject> eventClass : channelReg
					.getEvents()) {
				if (!eventMap.containsKey(eventClass)) {
					buildEvent(xmlConfig, eventClass);
				}
			}
		}

		
	}

	/**
	 * Build an Event into the configuration and add it to the eventMap
	 * 
	 * @param xmlConfig
	 **/
	private void buildEvent(KaEventConfig xmlConfig,
			Class<? extends EventObject> eventClass) {
		KaEventConfig.Events.Event event = new KaEventConfig.Events.Event();
		event.eventClass = eventClass.getName();
		Event eventAnnotation = eventClass.getAnnotation(Event.class);
		event.listenerInterface = eventAnnotation.listener().getName();
		event.name = eventClass.getName();
		xmlConfig.events.event.add(event);
		eventMap.put(eventClass, event);
	}

	/**
	 * Scan the supplied class path for event classes annotated with @Event
	 * 
	 * @param scanClassPath	class path to scan for events.
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder scan(String scanClassPath) {
		this.scanClassPath = scanClassPath;
		return this;
	}
	
	/**
	 * Set the BeanResolver implementation to use
	 * 
	 * @param beanResolverClass BeanResolver implementation
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder beanResolverClass(
			Class<? extends BeanResolver> beanResolverClass) {
		this.beanResolverClass = beanResolverClass;
		return this;
	}

	/**
	 * Sets the queue (worker thread) implementation to use. Overrides the
	 * default implementation.
	 * 
	 * @param queueClass	Queue Thread implementation class
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder queueClass(
			Class<? extends ThreadPoolQueueExecutor> queueClass) {
		this.queueClass = queueClass;
		return this;
	}

	/**
	 * Set the maximum pool size on the default Queue Thread implementation, set this to 
	 * 1 to get a pure serial event execution thread. Note this value will not have any effect
	 * if the default Queue Thread is overridden by queueClass().
	 * 
	 * @param maxPoolSize	The maximum number of threads to use.
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder maxPoolSize(byte maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	/**
	 * Set the core pool size on the default Queue Thread implementation. 
	 * Note this value will not have any effect if the default Queue Thread is overridden by queueClass().
	 * 
	 * @param corePoolSize	The number of threads to initialize the worker pool with.
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder corePoolSize(byte corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}

	/**
	 * Set the keep alive time on the default Queue Thread implementation. 
	 * Note this value will not have any effect if the default Queue Thread is overridden by queueClass().
	 * 
	 * @param keepAliveTime	Keep alive time in milliseconds, idle threads will be stopped after this timeout.
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder keepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
		return this;
	}

	/**
	 * Add a channel to the configuration.
	 * 
	 * @param channelName	Name of the channel
	 * @param events		Events this channel should handle
	 * 
	 * @return	the builder
	 **/
	public KaEventConfigBuilder addChannel(String channelName,
			Class<? extends EventObject>... events) {
		for(Class<? extends EventObject> eventClass : events) {
			if(!eventClass.isAnnotationPresent(Event.class)) {
				throw new IllegalArgumentException("Only events annotated with @Event can used in addChannel "+eventClass+" is not annotated with @Event");
			}
		}
		channelRegs.add(new ChannelReg(channelName, events));
		return this;
	}

	/**
	 * Add an event to the configuration.
	 * 
	 * @param eventClass	The class name of the event to add, must be annotated with @Event.
	 * 
	 * @return the builder
	 **/
	public KaEventConfigBuilder addEvent(Class<? extends EventObject> eventClass) {
		
		if(!eventClass.isAnnotationPresent(Event.class)) {
			throw new IllegalArgumentException("Only events annotated with @Event can used in addEvent "+eventClass+" is not annotated with @Event");
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
		private Class<? extends EventObject>[] events;

		public ChannelReg(String channelName,
				Class<? extends EventObject>[] events) {

			this.channelName = channelName;
			this.events = events;
		}

		public String getChannelName() {
			return channelName;
		}

		public Class<? extends EventObject>[] getEvents() {
			return events;
		}

	}
}
