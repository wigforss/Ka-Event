package org.kasource.kaevent.config;


//import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Configures the Ka-Event environment for spring.
 * 
 * @author rikardwi
 **/
public class SpringKaEventConfigurer extends KaEventConfigurer implements ApplicationContextAware {

	private KaEventConfiguration configuration;
	
	private String scanClassPath;
	
	private ApplicationContext applicationContext;
	
	private Map<Object, List<Object>> listeners;
	

	private Map<Object, List<EventFilter<EventObject>>> filterMap;
	 
	/**
	 * Constructor.
	 * 
	 * @param configuration Configuration object.
	 **/
	protected  SpringKaEventConfigurer(KaEventConfiguration configuration) {
		this.configuration = configuration;	
	}
	
	/**
	 * Configure the Ka-Event environment.
	 **/
	public void configure() {
		if (scanClassPath != null && scanClassPath.length() > 0) {
            importAndRegisterEvents(new AnnotationEventExporter(scanClassPath),
            					    configuration.getEventBuilderFactory(), 
            					    configuration.getEventRegister());
        }
		// Initialize Events, if lazy.
		applicationContext.getBeansOfType(EventConfig.class);
		registerSpringEvents(configuration.getEventBuilderFactory(), configuration.getEventRegister());
		// Initialize Channels, if lazy.
		applicationContext.getBeansOfType(Channel.class);
		registerListeners();
		registerEventsAtChannels(configuration.getEventRegister(), configuration.getChannelFactory(), configuration.getChannelRegister());
		KaEventInitializer.setConfiguration(configuration);
	}

	/**
	 * Register all source bean listeners.
	 **/
	private void registerListeners() {
		SourceObjectListenerRegister sourceObjectListenerRegister = 
			configuration.getSourceObjectListenerRegister();
    	if (listeners != null) {
    		for (Map.Entry<Object, List<Object>>  listenerEntry : listeners.entrySet()) {
    			for (Object listener : listenerEntry.getValue()) {
    				List<EventFilter<EventObject>> filters = getFilter(listener);
        			if (filters != null) {
        				sourceObjectListenerRegister
        					.registerListener(listener, 
        									  listenerEntry.getKey(), 
        									  filters);
        			} else {
        				sourceObjectListenerRegister.registerListener(listener, listenerEntry.getKey());
        			}
    			}
    		
    		}
    	}
    }
	
	private void registerSpringEvents(EventBuilderFactory eventBuilderFactory, EventRegister eventRegister) {
	    for(SpringEvent springEvent : SpringEvent.values()) {
	        eventRegister.registerEvent(eventBuilderFactory.getBuilder(springEvent.getEvent()).bindInterface(springEvent.getListener(), springEvent.getListenerMethod())
                        .bindAnnotation(springEvent.getMethodAnnotation())
                        .build());
	    }
	}
	
	/**
	 * Returns list of EventFilters for a listener.
	 * 
	 * @param listener	Listener to get filter for.
	 * 
	 * @return Filters for listener of  null if no filters found.
	 **/
	private List<EventFilter<EventObject>> getFilter(Object listener) {
		if (filterMap != null) {
			return filterMap.get(listener);
		}
		return null;
	}
	
	/**
	 * Set package name(s) to scan for classes in.
	 * 
	 * The scanClassPath can be a comma separated list
	 * of package names. Note that all sub packages will also
	 * be scanned.
	 * 
	 * @param scanClassPath Package name to scan for @Event annotated classes.
	 **/
	public void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}

	/**
	 * Set filters to use for listeners that listens to source objects.
	 * 
	 * @param filterMap Map of filters.
	 **/
	public void setFilterMap(Map<Object, List<EventFilter<EventObject>>> filterMap) {
		this.filterMap = filterMap;
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Set the listener to register as source object listeners.
	 * 
	 * @param listeners Listeners to add.
	 **/
	public void setListeners(Map<Object, List<Object>>  listeners) {
		this.listeners = listeners;		
	}

}
