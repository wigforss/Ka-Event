package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeListener;



//CHECKSTYLE:OFF
///CLOVER:OFF
@ApplicationScoped
//@ChannelListener("temperatureChannel")
public class CommandConsole /*implements TemperatureChangeListener*/ {
	
	public CommandConsole() {
    	initilaize();
	}

    @RegisterListener
    void initilaize() {
    	
    }
    
    /*
    @Override
    public void temperatureChanged(TemperatureChangeEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperature()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
        
    }
*/

    public void listenToTemperatureChanged(@Observes TemperatureChangeEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperature()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }
}
