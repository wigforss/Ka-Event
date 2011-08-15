package org.kasource.kaevent.example.guice.simple;


import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEvent;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;


public class ExampleModule extends KaEventModule{
	
	public ExampleModule(){
		
	}
	
	@Override
	protected void configure() {
		super.configure();
		bind(Thermometer.class).annotatedWith(Names.named("thermometer")).to(Thermometer.class);
	}
	

	
	@Provides
	@Named("temparatureEvent")
	EventConfig provideTempEvent(EventFactory eventFactory) {
		System.out.println("Provide event");
		return eventFactory.newFromAnnotatedEventClass(TemperatureChangeEvent.class, "temparatureEvent");
	}
}
