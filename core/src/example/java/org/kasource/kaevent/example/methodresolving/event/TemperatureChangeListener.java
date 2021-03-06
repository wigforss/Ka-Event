package org.kasource.kaevent.example.methodresolving.event;

import java.util.EventListener;

import org.kasource.kaevent.annotations.event.methodresolving.DefaultListenerMethod;
import org.kasource.kaevent.annotations.event.methodresolving.KeywordCase;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;


//CHECKSTYLE:OFF
///CLOVER:OFF
@MethodResolving(MethodResolverType.KEYWORD_SWITCH)
public interface TemperatureChangeListener extends EventListener {
	
	@DefaultListenerMethod
	@KeywordCase("UP")
	public void temperatureUp(TemperatureChangeEvent event);
	
	@KeywordCase("DOWN")
	public void temperatureDown(TemperatureChangeEvent event);
}
