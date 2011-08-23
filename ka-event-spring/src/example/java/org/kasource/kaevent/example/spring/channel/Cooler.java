package org.kasource.kaevent.example.spring.channel;

import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.channel.event.TemperatureChangedEventListener;


///CLOVER:OFF
public class Cooler implements TemperatureChangedEventListener {
    private boolean enabled = false;



 

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            if (!enabled) {
                System.out.println("Cooler  started.");
            }
            enabled = true;
        } else {
            if (enabled) {
                System.out.println("Cooler  turned off.");
            }
            enabled = false;
        }

    }

}
