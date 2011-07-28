package org.kasource.kaevent.config;



import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;




/**
 * Configures the Ka-Event environment for spring.
 * 
 * @author rikardwi
 **/
public class SpringKaEventConfigurer extends KaEventConfigurer implements ApplicationContextAware{

	private KaEventConfiguration configuration;
	
	private String scanClassPath;
	
	private ApplicationContext applicationContext;
	
	private Map<Object, List<EventListener>> listeners;
	

	private Map<EventListener, List<EventFilter<EventObject>>> filterMap;
	 
	protected  SpringKaEventConfigurer(KaEventConfiguration configuration){
		this.configuration = configuration;	
	}
	
	
	public void configure() {
		if(scanClassPath != null && scanClassPath.length() > 0) {
            importAndRegisterEvents(new AnnotationEventExporter(scanClassPath),configuration.getEventFactory(), configuration.getEventRegister());
        }
		// Initialize Events
		applicationContext.getBeansOfType(EventConfig.class);
		// Initialize Channels
		applicationContext.getBeansOfType(Channel.class);
		registerListeners();
		registerEventsAtChannels(configuration);
		KaEventInitializer.setConfiguration(configuration);
	}

	/**
	 * Register all source bean listeners.
	 **/
	private void registerListeners() {
		SourceObjectListenerRegister sourceObjectListenerRegister = configuration.getSourceObjectListenerRegister();
    	if(listeners != null) {
    		for(Map.Entry<Object, List<EventListener>>  listenerEntry : listeners.entrySet()) {
    			for(EventListener listener : listenerEntry.getValue()) {
    				List<EventFilter<EventObject>> filters = getFilter(listener);
        			if(filters != null) {
        				sourceObjectListenerRegister.registerListener(listener, listenerEntry.getKey(), filters);
        			} else {
        				sourceObjectListenerRegister.registerListener(listener, listenerEntry.getKey());
        			}
    			}
    		
    		}
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
		if(filterMap != null) {
			return filterMap.get(listener);
		}
		return null;
	}
	
	public void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}

	public void setFilterMap(Map<EventListener, List<EventFilter<EventObject>>> filterMap) {
		this.filterMap = filterMap;
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setListeners(Map<Object, List<EventListener>>  listeners) {
		this.listeners = listeners;		
	}

}
