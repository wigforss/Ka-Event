package org.kasource.kaevent.example.aspectj.channel;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventConfigurer;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;

/**
 * Example that demonstrate usage of Channels.
 * 
 * The CommandConsole class listens to the temperatureChannel instead of a
 * thermometer object.
 * 
 * @author Rikard Wigforss
 **/
// /CLOVER:OFF
public class ExampleRunner implements KaEventInitializedListener {

    private Thermometer thermometer;

    private ExampleRunner(Thermometer thermometer) {
        this.thermometer = thermometer;
        KaEventInitializer.getInstance().addListener(this);
    }

    public static void main(String[] args) {
        EventDispatcher eventDispatcher = new DefaultEventDispatcher("classpath:org/kasource/kaevent/example/channel/channel-config.xml");
        Thermometer thermometer = new Thermometer();
        new ExampleRunner(thermometer);
        Cooler cooler = new Cooler();
        Heater heater = new Heater();
        thermometer.setEventDispatcher(eventDispatcher);
        thermometer.setCooler(cooler);
        thermometer.setHeater(heater);
        new CommandConsole();
        thermometer.setOptimalTemperatur(25);

        new Thread(thermometer).start();
    }

    @Override
    public void doInitialize(KaEventConfiguration configuration) {
        BeanResolver beanResolver = configuration.getBeanResolver();
        CustomBeanResolver customBeanResolver = (CustomBeanResolver) beanResolver;
        customBeanResolver.putBean("thermometer", thermometer);
    }
}
