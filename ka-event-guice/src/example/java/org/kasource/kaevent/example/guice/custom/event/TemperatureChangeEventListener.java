package org.kasource.kaevent.example.guice.custom.event;

import java.util.EventListener;


import org.kasource.kaevent.annotations.event.methodresolving.BeanMethodResolver;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;

//CHECKSTYLE:OFF
///CLOVER:OFF
@MethodResolving(MethodResolverType.BEAN)
@BeanMethodResolver("tempratureMethodResolver")
public interface TemperatureChangeEventListener extends EventListener {
	 public void highTemperature(TemperatureChangeEvent event);
	 
	 public void mediumTemperature(TemperatureChangeEvent event);
	 
	 public void lowTemperature(TemperatureChangeEvent event);
}
