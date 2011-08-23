package org.kasource.kaevent.example.spring.channel;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    	  ApplicationContext context = 
    	      new ClassPathXmlApplicationContext("org/kasource/kaevent/example/spring/channel/channel-context.xml");
  	    Thermometer thermometer = (Thermometer) context.getBean("thermometer");
  	    thermometer.run();
    }

   
}
