package org.kasource.kaevent.example.guice.custom;

import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEventListener;

///CLOVER:OFF
public class Cooler implements TemperatureChangeEventListener {

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void highTemperature(TemperatureChangeEvent event) {
        if (enabled == false) {
            System.out.println("Cooler started.");
        }
        enabled = true;

    }

    @Override
    public void lowTemperature(TemperatureChangeEvent event) {
        if (enabled == true) {
            System.out.println("Cooler turned off.");
        }
        enabled = false;

    }

    @Override
    public void mediumTemperature(TemperatureChangeEvent event) {
        if (enabled == true) {
            System.out.println("Cooler turned off.");
        }
        enabled = false;

    }

}
