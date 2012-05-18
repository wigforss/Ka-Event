package org.kasource.kaevent.example.aspectj.simple;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;



/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner implements KaEventInitializedListener{
    private Thermometer thermometer;
    
    private ExampleRunner(Thermometer thermometer) {
        this.thermometer = thermometer;
    }
    
	public static void main(String[] args) {
	    
		Thermometer thermometer = new Thermometer();
		
		EventDispatcher eventDispatcher = new DefaultEventDispatcher("org/kasource/kaevent/example/simple/simple-config.xml");
		KaEventInitializer.getInstance().addListener(new ExampleRunner(thermometer));
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		new Thread(thermometer).start();
	}

   
    @Override
    public void doInitialize(KaEventConfiguration configuration) {
        BeanResolver beanResolver = configuration.getBeanResolver();
        ((CustomBeanResolver) beanResolver).putBean("thermometer", thermometer);
        
    }
}
