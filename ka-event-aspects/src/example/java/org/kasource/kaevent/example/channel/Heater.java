package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEventListener;
import org.kasource.kaevent.listener.RegisterListener;
import org.kasource.kaevent.listener.implementations.BeanListener;

///CLOVER:OFF
@BeanListener("thermometer")
public class Heater implements TemperatureChangedEventListener {
    private boolean enabled = false;

    public Heater() {
        initialize();
    }

    @RegisterListener
    private void initialize() {
    }

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
