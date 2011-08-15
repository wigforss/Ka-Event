package org.kasource.kaevent.example.guice.channel;


import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;


import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;


public class ExampleModule extends KaEventModule {
	
	
	
	
	public ExampleModule(){
		
	}
	
	@Override
	protected void configure() {
		super.configure();
		bind(Thermometer.class).annotatedWith(Names.named("thermometer")).to(Thermometer.class);
		setScanClassPath(ExampleModule.class.getPackage().getName());
	}
	
	
	
	
	@Provides
	@Named("temperatureChannel")
	ListenerChannel provideTempChannel(ChannelFactory channelFactory) {
		Channel channel = channelFactory.createChannel("temperatureChannel");
		channel.registerEvent(TemperatureChangedEvent.class);
		return (ListenerChannel) channel;
	}
	
}
