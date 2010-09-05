package org.kasource.kaevent.osgi;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.kasource.commons.osgi.OsgiUtils;
import org.kasource.commons.reflection.AnnotationClassFilter;
import org.kasource.commons.reflection.AssignableFromClassFilter;
import org.kasource.commons.reflection.FilterList;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.implementations.ChannelListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator, BundleListener, KaEventInitializedListener{

	private BundleContext bundleContext;
	private EventDispatcher eventDispatcher;
	private EventRegister eventRegister;
	private EventFactory eventFactory;
	private ChannelFactory channelFactory;
	private ChannelRegister channelRegister;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		initialize();
		
	}

	private void initialize() {
		 KaEventInitializer.getInstance().addListener(this);
		 eventDispatcher = new DefaultEventDispatcher();
		 bundleContext.addBundleListener(this);
		 Bundle[] bundles = bundleContext.getBundles();
	        for (Bundle bundle : bundles) {
	            if (bundle.getState() == Bundle.ACTIVE) {
	            	registerEvents(bundle);
	            }
	        }
	        for (Bundle bundle : bundles) {
	            if (bundle.getState() == Bundle.ACTIVE) {
	            	registerListeners(bundle);
	            }
	        }
	        bundleContext.registerService(new String[]{EventDispatcher.class.getName()}, eventDispatcher, new Properties());
	       
	}
	
	
	@Override
	public void stop(BundleContext context) throws Exception {
		this.bundleContext = null;
		
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		 Bundle bundle = event.getBundle();
	     if (event.getType() == BundleEvent.STARTED) {
	        	registerEvents(bundle);
	        	registerListeners(bundle);
	     } else if (event.getType() == BundleEvent.STOPPED) {
	        	unregisterEvents(bundle);
	     }
	       
	}

	
	
	@SuppressWarnings("unchecked")
	private void registerEvents(Bundle bundle) {
		Set<Class<?>> eventClasses = getEventClasses(bundle);
		for(Class<?> eventClass : eventClasses) {
			System.out.println("Register Event: "+eventClass);
			EventConfig event = eventFactory.newFromAnnotatedEventClass((Class<? extends EventObject>)eventClass);
			eventRegister.registerEvent(event);
			Event eventAnnotation = eventClass.getAnnotation(Event.class);
			String[] channels = eventAnnotation.channels();
			boolean autoCreateChannel = eventAnnotation.createChannels();
			for(String channelName : channels) {
				try {
					Channel channel = channelRegister.getChannel(channelName);
					channel.registerEvent((Class<? extends EventObject>) eventClass);
				}catch(NoSuchChannelException nsce) {
					if(autoCreateChannel) {
						Channel channel = channelFactory.createChannel(channelName);
						channel.registerEvent((Class<? extends EventObject>) eventClass);
					} else {
						// Log
					}
				}
			}
		}
	}
	
	private Set<Class<?>>  getEventClasses(Bundle bundle) {
		return OsgiUtils.getClasses(bundleContext, bundle, new FilterList(new AssignableFromClassFilter(EventObject.class), new AnnotationClassFilter(Event.class)));
	}
	
	private void registerListeners(Bundle bundle) {
		
		
		Set<Class<? extends EventListener>> registeredInterfaces = eventRegister.getListenersInterfaces();
		Set<String> interfaceNames = new HashSet<String>();
		for(Class<? extends EventListener> listenerClass : registeredInterfaces) {
			interfaceNames.add(listenerClass.getName());
		}
		Set<ServiceReference> listenerServices = OsgiUtils.getServices(bundle, interfaceNames);
		for(ServiceReference listenerRef : listenerServices){
			EventListener listener = (EventListener) bundleContext.getService(listenerRef);
			ChannelListener channelListener = listener.getClass().getAnnotation(ChannelListener.class);
			if(channelListener != null) {
				String[] channels = channelListener.value();
				for(String channelName : channels) {
					try {
						Channel channel = channelRegister.getChannel(channelName);
						channel.registerListener(listener);
					} catch(NoSuchChannelException nse) {
						// Log
					}
				}
			} else {
				// Log no annotation
				bundleContext.ungetService(listenerRef);
			}
		}
		
	}
	
	private void unregisterEvents(Bundle bundle) {
		Set<Class<?>> eventClasses = getEventClasses(bundle);
		for(Class<?> eventClass : eventClasses) {
			Set<Channel> channels = channelRegister.getChannelsByEvent((Class<? extends EventObject>)eventClass);
			for(Channel channel : channels) {
				channel.unregisterEvent((Class<? extends EventObject>)eventClass);
			}
		}
		// Should run unget on all removed listeners
	}

	@Override
	public void doInitialize(KaEventConfiguration configuration) {
		this.eventRegister = configuration.getEventRegister();
		this.eventFactory = configuration.getEventFactory();
		this.channelFactory = configuration.getChannelFactory();
		this.channelRegister = configuration.getChannelRegister();
	}
}
