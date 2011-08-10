package org.kasource.kaevent.example.guice.simple;


import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEvent;

import com.google.inject.Provides;
import com.google.inject.name.Named;


public class ExampleModule extends KaEventModule{
	
	public ExampleModule(){
		
	}
	
	@Override
	protected void configure() {
		super.configure();
	}
	
	@Provides
	@Named("temparatureEvent")
	EventConfig provideTempEvent(EventFactory eventFactory) {
		System.out.println("Provide event");
		return eventFactory.newFromAnnotatedEventClass(TemperatureChangeEvent.class, "temparatureEvent");
	}
}
