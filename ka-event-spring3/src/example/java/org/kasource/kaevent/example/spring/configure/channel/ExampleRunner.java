package org.kasource.kaevent.example.spring.configure.channel;


import org.kasource.kaevent.config.KaEventConfigurationClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Example that demonstrate usage of Channels.
 * 
 * The CommandConsole class listens to the temperatureChannel instead of a
 * thermometer object.
 * 
 * The channel-context.xml uses the kaevent xml namespace.
 * 
 * @author Rikard Wigforss
 **/
///CLOVER:OFF
//CHECKSTYLE:OFF
public class ExampleRunner  {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        KaEventConfigurationClass.scan(ExampleRunner.class.getPackage().getName());
        context.register(KaEventConfigurationClass.class);
        context.scan(ExampleRunner.class.getPackage().getName());
        context.refresh();
  	    Thermometer thermometer = (Thermometer) context.getBean("thermometer");
  	    thermometer.run();
    }

   
}
