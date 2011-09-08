package org.kasource.kaevent.example.spring.channel;

import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedListener;
import org.kasource.kaevent.spring.context.event.ContextRefreshedListener;
import org.kasource.kaevent.spring.context.event.ContextStartedListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;


///CLOVER:OFF
//CHECKSTYLE:OFF
public class CommandConsole implements TemperatureChangedListener, ContextStartedListener, ContextRefreshedListener {

 
    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperature()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

    @Override
    public void onContextStarted(ContextStartedEvent event) {
        System.out.println(event);
        
    }

    @Override
    public void onContextRefreshed(ContextRefreshedEvent event) {
        System.out.println(event);
    }


}
