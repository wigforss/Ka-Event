package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEventListener;

///CLOVER:OFF
@BeanListener("thermometer")
public class Cooler implements TemperatureChangedEventListener {
    private boolean enabled = false;

    public Cooler() {
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
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            if (enabled == false) {
                System.out.println("Cooler  started.");
            }
            enabled = true;
        } else {
            if (enabled == true) {
                System.out.println("Cooler  turned off.");
            }
            enabled = false;
        }

    }

}
