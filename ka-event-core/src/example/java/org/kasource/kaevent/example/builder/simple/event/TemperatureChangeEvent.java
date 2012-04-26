package org.kasource.kaevent.example.builder.simple.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.builder.simple.Thermometer;


//CHECKSTYLE:OFF
///CLOVER:OFF
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
