package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEventListener;

import com.google.inject.Inject;
import com.google.inject.name.Named;


///CLOVER:OFF

public class CommandConsole implements TemperatureChangedEventListener {

    @Inject
	public CommandConsole(@Named("temperatureChannel") Channel channel){
		channel.registerListener(this);
	}

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}