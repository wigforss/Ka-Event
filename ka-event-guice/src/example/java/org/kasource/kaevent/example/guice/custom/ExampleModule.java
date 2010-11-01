package org.kasource.kaevent.example.guice.custom;

import java.util.EventObject;

import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.event.method.MethodResolver;


import com.google.inject.Provides;
import com.google.inject.name.Named;


public class ExampleModule extends KaEventModule{
	
	
	
	
	public ExampleModule(){
		super(new KaEventConfigBuilder().scan(ExampleModule.class.getPackage().getName()).build());
	}
	
	@Override
	protected void configure() {
		super.configure();
	}
	
	@Provides
	@Named("tempratureMethodResolver")
	MethodResolver<? extends EventObject> provideTempChannel() {
		return  new TempratureMethodResolver(21.0d);
	}
}
