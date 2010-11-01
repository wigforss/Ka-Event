package org.kasource.kaevent.example.guice.simple;



import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * Simple example that demonstrates listening to source objects.
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
