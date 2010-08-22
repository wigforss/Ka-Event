package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEventListener;
import org.kasource.kaevent.listener.ChannelListener;
import org.kasource.kaevent.listener.RegisterListener;

///CLOVER:OFF
@ChannelListener("temperatureChannel")
public class CommandConsole implements TemperatureChangedEventListener {

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
