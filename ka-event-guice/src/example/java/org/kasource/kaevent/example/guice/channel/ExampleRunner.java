package org.kasource.kaevent.example.guice.channel;


import org.kasource.kaevent.example.guice.channel.Thermometer;
import org.kasource.kaevent.example.guice.channel.ExampleModule;


import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Example that demonstrate usage of Channels.
 * 
 * The CommandConsole class listens to the temperatureChannel instead of a
 * thermometer object.
 * 
 * @author Rikard Wigforss
 **/
// /CLOVER:OFF
public class ExampleRunner  {

    

    public static void main(String[] args) {
    	Injector injector = Guice.createInjector(new ExampleModule());
		Thermometer thermometer = injector.getInstance(Thermometer.class);
		injector.getInstance(CommandConsole.class);
		thermometer.registerListers();
		new Thread(thermometer).start();
    }

   
}
