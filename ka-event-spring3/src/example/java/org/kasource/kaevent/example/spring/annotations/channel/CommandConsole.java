package org.kasource.kaevent.example.spring.annotations.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;




///CLOVER:OFF
//CHECKSTYLE:OFF
@ChannelListener("temperatureChannel")
@Component
public class CommandConsole implements TemperatureChangedListener, ApplicationListener<ContextRefreshedEvent> {

 
    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(event.getApplicationContext().toString());
        
    }

  

}
