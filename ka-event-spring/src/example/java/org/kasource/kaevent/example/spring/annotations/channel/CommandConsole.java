package org.kasource.kaevent.example.spring.annotations.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEventListener;
import org.springframework.stereotype.Component;


///CLOVER:OFF
//CHECKSTYLE:OFF
@ChannelListener("temperatureChannel")
@Component
public class CommandConsole implements TemperatureChangedEventListener {

 
    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}
