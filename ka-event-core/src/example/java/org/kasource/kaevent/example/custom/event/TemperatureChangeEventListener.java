package org.kasource.kaevent.example.custom.event;

import java.util.EventListener;

import org.kasource.kaevent.annotations.event.methodresolving.FactoryMethodResolver;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.example.custom.TempratureMethodResolver;

//CHECKSTYLE:OFF
///CLOVER:OFF
@MethodResolving(MethodResolverType.FACTORY)
@FactoryMethodResolver(factoryClass = TempratureMethodResolver.class, factoryMethod = "getInstance")
public interface TemperatureChangeEventListener extends EventListener {
	 public void highTemperature(TemperatureChangeEvent event);
	 
	 public void mediumTemperature(TemperatureChangeEvent event);
	 
	 public void lowTemperature(TemperatureChangeEvent event);
}
