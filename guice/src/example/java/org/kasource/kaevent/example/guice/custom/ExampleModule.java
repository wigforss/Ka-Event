package org.kasource.kaevent.example.guice.custom;


import org.kasource.kaevent.config.KaEventModule;
import org.kasource.kaevent.event.method.MethodResolver;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class ExampleModule extends KaEventModule {
	
	public ExampleModule() {
		
	}
	
	@Override
	protected void configure() {
		super.configure();
		bind(Thermometer.class).annotatedWith(Names.named("thermometer")).to(Thermometer.class);
		setScanClassPath(ExampleModule.class.getPackage().getName());
	}
	
	@SuppressWarnings("rawtypes")
	@Provides
	@Named("tempratureMethodResolver")
	MethodResolver provideTempChannel() {
		return  new TempratureMethodResolver(21.0d);
	}
}
