package org.kasource.kaevent.example.guice.simple;


import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEvent;


public class ExampleModule extends KaEventModule{
	
	public ExampleModule(){
		super(new KaEventConfigBuilder().addEvent(TemperatureChangeEvent.class).build());
	}
	
	@Override
	protected void configure() {
		super.configure();
	}
}
