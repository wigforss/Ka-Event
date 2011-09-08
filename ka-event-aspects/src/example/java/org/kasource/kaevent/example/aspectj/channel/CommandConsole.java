package org.kasource.kaevent.example.aspectj.channel;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.aspectj.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.aspectj.channel.event.TemperatureChangedListener;

///CLOVER:OFF
@ChannelListener("temperatureChannel")
public class CommandConsole implements TemperatureChangedListener {

    public CommandConsole() {
        initialize();
    }

    @RegisterListener
    private void initialize() {
    }

    @Override
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
            System.out.println("Warning " + event.getSource() + " overheating!");
        }
    }

}
