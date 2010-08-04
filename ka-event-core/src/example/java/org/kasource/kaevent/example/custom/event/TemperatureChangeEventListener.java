package org.kasource.kaevent.example.custom.event;

import java.util.EventListener;

import org.kasource.kaevent.example.custom.TempratureMethodResolver;
import org.kasource.kaevent.listener.interfaces.FactoryMethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.listener.interfaces.MethodResolving;


@MethodResolving(MethodResolverType.FACTORY)
@FactoryMethodResolver(factoryClass=TempratureMethodResolver.class,factoryMethod="getInstance")
public interface TemperatureChangeEventListener extends EventListener{
	 public void highTemperature(TemperatureChangeEvent event);
	 
	 public void mediumTemperature(TemperatureChangeEvent event);
	 
	 public void lowTemperature(TemperatureChangeEvent event);
}
