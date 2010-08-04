package org.kasource.kaevent.example.custom.event;

import java.util.EventObject;

import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.example.custom.Thermometer;

@Event(listener = TemperatureChangeEventListener.class)
public class TemperatureChangeEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private double currentTemperature;

    public TemperatureChangeEvent(Thermometer source, double currentTemperature) {
        super(source);
        this.currentTemperature = currentTemperature;
    }

    @Override
    public Thermometer getSource() {
        return (Thermometer) super.getSource();
    }

    public double getCurrentTemperature() {
        return this.currentTemperature;
    }

}
