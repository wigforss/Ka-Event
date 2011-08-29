package org.kasource.kaevent.example.spring.channel;

import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedEventListener;


///CLOVER:OFF
//CHECKSTYLE:OFF
public class CommandConsole implements TemperatureChangedEventListener {

 
    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperature()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}
