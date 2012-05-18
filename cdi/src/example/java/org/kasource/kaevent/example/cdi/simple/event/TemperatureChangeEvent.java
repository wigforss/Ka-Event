package org.kasource.kaevent.example.cdi.simple.event;


import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.example.cdi.simple.Thermometer;

//CHECKSTYLE:OFF
///CLOVER:OFF
@Event(listener = TemperatureChangeListener.class, annotation = OnTemperatureChange.class, channels="temperatureChannel")
public class TemperatureChangeEvent extends BaseEvent {
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
