package org.kasource.kaevent.example.guice.custom.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.guice.custom.Thermometer;

//CHECKSTYLE:OFF
///CLOVER:OFF
@Event(listener = TemperatureChangeListener.class)
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
