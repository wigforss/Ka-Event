package org.kasource.kaevent.config;



import java.util.EventListener;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;





public class SpringKaEventConfigurer extends KaEventConfigurer implements ApplicationContextAware{

	private KaEventConfiguration configuration;
	
	private String scanClassPath;
	
	private ApplicationContext applicationContext;
	
	 private Map<Object, List<EventListener>> listeners;
	
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

	
	private void registerListeners() {
		SourceObjectListenerRegister sourceObjectListenerRegister = configuration.getSourceObjectListenerRegister();
    	if(listeners != null) {
    		for(Map.Entry<Object, List<EventListener>>  listenerEntry : listeners.entrySet()) {
    			for(EventListener listener : listenerEntry.getValue()) {
    				sourceObjectListenerRegister.registerListener(listener, listenerEntry.getKey());
    			}
    		
    		}
    	}
    }
	
	public void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
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
