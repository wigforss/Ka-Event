package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEventListener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;


///CLOVER:OFF
@Singleton
@ChannelListener("temperatureChannel")
public class CommandConsole implements TemperatureChangedEventListener {
	
	public CommandConsole(){
    	initilaize();
	}

    @RegisterListener
    void initilaize() {
    	
    }
    
    
    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}
