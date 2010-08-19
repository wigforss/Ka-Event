package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotationImpl;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Thermometer thermometer = new Thermometer();
		EventDispatcher eventDispatcher = new DefaultEventDispatcher(ExampleRunner.class.getPackage().getName().replace('.', '/')+"/simple-config.xml");	
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		// RegisterListenerByAnnotationImpl.getInstance().initialize(channelRegister, sourceObjectListenerRegister, beanResolver)
		new Thread(thermometer).start();
	}
}
