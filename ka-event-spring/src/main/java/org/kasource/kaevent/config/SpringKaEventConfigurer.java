package org.kasource.kaevent.config;



import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;





public class SpringKaEventConfigurer extends KaEventConfigurer implements ApplicationContextAware{

	private KaEventConfiguration configuration;
	
	private String scanClassPath;
	
	private ApplicationContext applicationContext;
	
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
		
		registerEventsAtChannels(configuration);
		KaEventInitializer.setConfiguration(configuration);
	}

	public void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
