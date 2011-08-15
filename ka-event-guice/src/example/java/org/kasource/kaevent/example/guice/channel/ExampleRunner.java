package org.kasource.kaevent.example.guice.channel;




import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

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
    	Thermometer thermometer = injector.getInstance(Key.get(Thermometer.class, Names.named("thermometer")));
		injector.getInstance(CommandConsole.class);
		
		new Thread(thermometer).start();
    }

   
}
