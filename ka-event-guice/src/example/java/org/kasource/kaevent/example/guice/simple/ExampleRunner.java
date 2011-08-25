package org.kasource.kaevent.example.guice.simple;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
//CHECKSTYLE:OFF
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ExampleModule());
		
		Thermometer thermometer = injector.getInstance(Key.get(Thermometer.class, Names.named("thermometer")));
	
		new Thread(thermometer).start();
	}
	
	
}
