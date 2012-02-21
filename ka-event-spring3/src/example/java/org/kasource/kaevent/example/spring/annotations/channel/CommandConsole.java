package org.kasource.kaevent.example.spring.annotations.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.example.spring.annotations.channel.event.OnTemperatureChange;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.spring.context.event.OnContextRefreshed;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;




///CLOVER:OFF
//CHECKSTYLE:OFF
@ChannelListener({"temperatureChannel","springChannel"})
@Component
public class CommandConsole {

 
    @OnTemperatureChange
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

    @OnContextRefreshed
    public void onContextRefreshed(ContextRefreshedEvent event) {
        System.out.println(event.getApplicationContext().toString());
        
    }
  
}
