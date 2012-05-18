package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeEvent;


@ApplicationScoped @Named("thermometerSimple") 
public class Thermometer {

	private double optimalTemperature  = 22.0d;
	private double currentTemperature  = 0.0d;
	
	@Inject
	private Heater heater;
	
	@Inject
	private Cooler cooler;
	
	@Inject
	private EventDispatcher eventDispatcher;
	
	public void run() {
	    System.out.println("Run: " + this);
		for (int i = 0; i < 100; ++i) {
			if (cooler.isEnabled()) {
				currentTemperature -= Math.random() * 3.0d;
			} else if (heater.isEnabled()) {
				currentTemperature += Math.random() * 3.0d;
			} else {
				currentTemperature += 1.0d;
			}
			System.out.println("Thermometer  temp is now: " + currentTemperature);
			eventDispatcher.fireBlocked(new TemperatureChangeEvent(this, currentTemperature));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    /**
     * @return the optimalTemperature
     */
    protected double getOptimalTemperature() {
        return optimalTemperature;
    }
}
