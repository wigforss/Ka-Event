package org.kasource.kaevent.example.methodresolving;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.methodresolving.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.methodresolving.event.TemperatureDirection;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class Thermometer implements Runnable {
	
	private double optimalTemperatur = 22.0d;
	private double currentTemperatur = 0.0d;
	
	private Cooler cooler;
	private Heater heater;
	private EventDispatcher eventDispatcher;
	
	

	public void registerListers() {
		eventDispatcher.registerListener(cooler, this);
		eventDispatcher.registerListener(heater, this);
	}
	

	public double getOptimalTemperatur() {
		return optimalTemperatur;
	}

	public void setOptimalTemperatur(double optimalTemperatur) {
		this.optimalTemperatur = optimalTemperatur;
	}
	
	
	
	
	public void run() {
		for (int i = 0; i < 100; ++i) {
			TemperatureDirection direction = TemperatureDirection.UP;
			if (cooler.isEnabled()) {
				currentTemperatur -= Math.random() * 3.0d;
				direction = TemperatureDirection.DOWN;
			} else if (heater.isEnabled()) {
				currentTemperatur += Math.random() * 3.0d;
			} else {
				currentTemperatur += 1.0d;
			}
			System.out.println("Temp is now: " + currentTemperatur + " " + direction);
			eventDispatcher.fireBlocked(new TemperatureChangeEvent(this, currentTemperatur, direction));
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
}
