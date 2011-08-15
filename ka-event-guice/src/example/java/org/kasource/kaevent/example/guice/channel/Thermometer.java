package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;

import com.google.inject.Inject;

///CLOVER:OFF
public class Thermometer implements Runnable{
	
	
	private double optimalTemperatur = 22.0d;
	private double currentTemperatur = 0.0d;
	
	@Inject
	private Cooler cooler;
	@Inject
	private Heater heater;
	@Inject
	private EventDispatcher eventDispatcher;
	
	public Thermometer() {
		
	}
	
	

	public double getOptimalTemperatur() {
		return optimalTemperatur;
	}

	public void setOptimalTemperatur(double optimalTemperatur) {
		this.optimalTemperatur = optimalTemperatur;
	}
	
	
	
	
	public void run() {
		for(int i= 0; i < 100; ++i) {
			if(cooler.isEnabled()) {
				currentTemperatur -= Math.random()*3.0d;
			}else if(heater.isEnabled()) {
				currentTemperatur += Math.random()*3.0d;
			} else {
				currentTemperatur += 1.0d;
			}
			System.out.println("Thermometer  temp is now: "+currentTemperatur);
			eventDispatcher.fireBlocked(new TemperatureChangedEvent(this, currentTemperatur));
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
