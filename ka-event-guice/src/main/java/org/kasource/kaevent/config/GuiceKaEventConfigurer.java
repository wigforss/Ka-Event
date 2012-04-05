package org.kasource.kaevent.config;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.register.EventRegister;



import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.internal.Nullable;
import com.google.inject.name.Named;

/**
 * Configures the Ka-Event environment with Guice.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceKaEventConfigurer extends KaEventConfigurer {
	
	@Inject
	private Injector injector;
	
	@Inject
	private EventRegister eventRegister;
	
	@Inject
	private EventBuilderFactory eventBuilderFactory;
	
	@Nullable
	@Inject
	@Named("kaEvent.scan.package")
	private String scanClassPath;
	
	/**
	 * Configure the Ka-Event environment.
	 **/
	public void configure() {
		registerEvents();
		if (scanClassPath != null && scanClassPath.length() > 0) {
            importAndRegisterEvents(new AnnotationEventExporter(scanClassPath), eventBuilderFactory, eventRegister);
        }
		createChannels();
		//creating the configuration will cause  
		// notification to listeners that the environment is ready
		injector.getInstance(KaEventConfiguration.class);
	}
	
	/**
	 * Register provided events.
	 **/
	private void registerEvents() {
		for (Key<?> bindingKey : injector.getBindings().keySet()) {
			if (bindingKey.getTypeLiteral().getRawType().equals(EventConfig.class)) {
				EventConfig event = (EventConfig) injector.getInstance(bindingKey);
				eventRegister.registerEvent(event);
			}
			
		}
	}
	
	/**
	 * Create instances of provided channels.
	 **/
	private void createChannels() {
		for (Key<?> bindingKey : injector.getBindings().keySet()) {
			if (Channel.class.isAssignableFrom(bindingKey.getTypeLiteral().getRawType())) {
				injector.getInstance(bindingKey);
			}
			
		}
	}
}
