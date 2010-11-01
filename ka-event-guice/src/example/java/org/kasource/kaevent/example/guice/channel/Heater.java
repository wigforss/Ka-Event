package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEventListener;



///CLOVER:OFF

public class Heater implements TemperatureChangedEventListener {
    private boolean enabled = false;

   


    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur()) {
            if (enabled == false) {
                System.out.println("Heater started.");
            }
            enabled = true;
        } else {
            if (enabled == true) {
                System.out.println("Heater turned off.");
            }
            enabled = false;
        }

    }
}
