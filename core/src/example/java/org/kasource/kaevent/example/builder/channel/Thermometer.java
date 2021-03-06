package org.kasource.kaevent.example.builder.channel;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.builder.channel.event.TemperatureChangedEvent;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class Thermometer implements Runnable {
	private static int counter = 0;
	
	private double optimalTemperatur = 22.0d;
	private double currentTemperatur = 0.0d;
	
	private Cooler cooler;
	private Heater heater;
	private EventDispatcher eventDispatcher;
	private int id;
	
	
	

	public Thermometer() {
		this.id = counter++;
	}
	

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
	    final int numberOfInvocations = 100;
		for (int i = 0; i < numberOfInvocations; ++i) {
			if (cooler.isEnabled()) {
				currentTemperatur -= Math.random() * 3.0d;
			} else if (heater.isEnabled()) {
				currentTemperatur += Math.random() * 3.0d;
			} else {
				currentTemperatur += 1.0d;
			}
			System.out.println("Thermometer " + id + " temp is now: " + currentTemperatur);
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
	
	public int getId() {
		return id;
	}

	
	@Override
	public String toString() {
		return "Thermometer " + id;
	}
}
