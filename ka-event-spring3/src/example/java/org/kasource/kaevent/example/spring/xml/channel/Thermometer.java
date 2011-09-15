package org.kasource.kaevent.example.spring.xml.channel;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.spring.xml.channel.event.TemperatureChangedEvent;

///CLOVER:OFF
//CHECKSTYLE:OFF
public class Thermometer implements Runnable {
	
	
	private double optimalTemperature  = 22.0d;
	private double currentTemperature  = 0.0d;
	
	private Cooler cooler;
	private Heater heater;
	private EventDispatcher eventDispatcher;
		

	public Thermometer() {
		
	}
	


	public double getOptimalTemperature() {
		return optimalTemperature;
	}

	public void setOptimalTemperatur(double optimalTemperature) {
		this.optimalTemperature = optimalTemperature;
	}
	
	
	
	
	public void run() {
		for (int i = 0; i < 100; ++i) {
			if (cooler.isEnabled()) {
				currentTemperature -= Math.random() * 3.0d;
			} else if (heater.isEnabled()) {
				currentTemperature += Math.random() * 3.0d;
			} else {
				currentTemperature += 1.0d;
			}
			System.out.println("Thermometer  temp is now: " + currentTemperature);
			eventDispatcher.fireBlocked(new TemperatureChangedEvent(this, currentTemperature));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setCooler(Cooler cooler) {
		this.cooler = cooler;
	}

	public void setHeater(Heater heater) {
		this.heater = heater;
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	
	
	
	@Override
	public String toString() {
		return "Thermometer";
	}
}
