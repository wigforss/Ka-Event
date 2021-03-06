package org.kasource.kaevent.example.spring.annotations.channel;

import javax.annotation.Resource;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.spring.annotations.channel.event.TemperatureChangedEvent;
import org.springframework.stereotype.Component;

///CLOVER:OFF
//CHECKSTYLE:OFF
@Component("thermometer")
public class Thermometer implements Runnable {
	
	
	private double optimalTemperatur = 22.0d;
	private double currentTemperatur = 0.0d;
	
	@Resource
	private Cooler cooler;
	
	@Resource
	private Heater heater;
	
	@Resource
	private EventDispatcher eventDispatcher;
		

	public double getOptimalTemperatur() {
		return optimalTemperatur;
	}

	public void setOptimalTemperatur(double optimalTemperatur) {
		this.optimalTemperatur = optimalTemperatur;
	}
	
	
	public void run() {
		for (int i = 0; i < 100; ++i) {
			if (cooler.isEnabled()) {
				currentTemperatur -= Math.random() * 3.0d;
			} else if (heater.isEnabled()) {
				currentTemperatur += Math.random() * 3.0d;
			} else {
				currentTemperatur += 1.0d;
			}
			System.out.println("Thermometer  temp is now: " + currentTemperatur);
			eventDispatcher.fireBlocked(new TemperatureChangedEvent(this, currentTemperatur));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "Thermometer";
	}
}
