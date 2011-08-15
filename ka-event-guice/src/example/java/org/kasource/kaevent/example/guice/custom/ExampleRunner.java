package org.kasource.kaevent.example.guice.custom;



import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Example which uses a custom method resolver found by invoking a factory method, see TemperatureChangeEventListener for details. 
 *  
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ExampleModule());
		Thermometer thermometer = injector.getInstance(Key.get(Thermometer.class, Names.named("thermometer")));
	

		new Thread(thermometer).start();
		
	}
}
