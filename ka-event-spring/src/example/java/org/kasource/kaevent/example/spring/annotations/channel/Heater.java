package org.kasource.kaevent.example.spring.annotations.channel;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedListener;
import org.springframework.stereotype.Component;

///CLOVER:OFF
//CHECKSTYLE:OFF
@BeanListener("thermometer")
@Component
public class Heater implements TemperatureChangedListener {
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur()) {
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
