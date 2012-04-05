package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.channel.event.OnTemperatureChanged;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;

import com.google.inject.Singleton;

//CHECKSTYLE:OFF
///CLOVER:OFF
@Singleton
@ChannelListener("temperatureChannel")
public class CommandConsole {
	
	public CommandConsole() {
    	initilaize();
	}

    @RegisterListener
    void initilaize() {
    	
    }
    
    
    @OnTemperatureChanged
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}
