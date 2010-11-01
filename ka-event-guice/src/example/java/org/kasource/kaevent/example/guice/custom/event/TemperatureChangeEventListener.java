package org.kasource.kaevent.example.guice.custom.event;

import java.util.EventListener;


import org.kasource.kaevent.listener.interfaces.BeanMethodResolver;

import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.listener.interfaces.MethodResolving;


@MethodResolving(MethodResolverType.BEAN)
@BeanMethodResolver("tempratureMethodResolver")
public interface TemperatureChangeEventListener extends EventListener{
	 public void highTemperature(TemperatureChangeEvent event);
	 
	 public void mediumTemperature(TemperatureChangeEvent event);
	 
	 public void lowTemperature(TemperatureChangeEvent event);
}
