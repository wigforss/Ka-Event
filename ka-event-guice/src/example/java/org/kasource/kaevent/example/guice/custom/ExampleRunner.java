package org.kasource.kaevent.example.guice.custom;





import org.kasource.kaevent.example.guice.custom.ExampleModule;
import org.kasource.kaevent.example.guice.custom.Thermometer;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Example which uses a custom method resolver found by invoking a factory method, see TemperatureChangeEventListener for details. 
 *  
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ExampleModule());
		Thermometer thermometer = injector.getInstance(Thermometer.class);
	
		thermometer.registerListers();
		new Thread(thermometer).start();
		
	}
}
