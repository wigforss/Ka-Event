package org.kasource.kaevent.example.spring.xml.channel;

import org.kasource.kaevent.example.spring.xml.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.xml.channel.event.TemperatureChangedListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public class Heater implements TemperatureChangedListener {
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() < event.getSource().getOptimalTemperature()) {
            if (!enabled) {
                System.out.println("Heater started.");
            }
            enabled = true;
        } else {
            if (enabled) {
                System.out.println("Heater turned off.");
            }
            enabled = false;
        }

    }
}
