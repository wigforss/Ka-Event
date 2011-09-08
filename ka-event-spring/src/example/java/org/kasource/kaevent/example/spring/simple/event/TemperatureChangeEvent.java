package org.kasource.kaevent.example.spring.simple.event;

import java.util.EventObject;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.spring.simple.Thermometer;

///CLOVER:OFF
//CHECKSTYLE:OFF
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
     
     public String getKeyword() {
    	 return "TWO";
     }

}
