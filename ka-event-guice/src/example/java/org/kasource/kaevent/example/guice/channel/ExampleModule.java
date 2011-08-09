package org.kasource.kaevent.example.guice.channel;


import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;

import com.google.inject.Provides;
import com.google.inject.name.Named;


public class ExampleModule extends KaEventModule {
	
	
	
	
	public ExampleModule(){
		super(new KaEventConfigBuilder().scan(ExampleModule.class.getPackage().getName()).build());
	}
	
	@Override
	protected void configure() {
		super.configure();
	}
	
	@Provides
	@Named("temperatureChannel")
	Channel provideTempChannel() {
		ChannelFactory channelFactory = configuration.getChannelFactory();
		Channel channel = channelFactory.createChannel("temperatureChannel");
		channel.registerEvent(TemperatureChangedEvent.class);
		return channel;
	}
}
