package org.kasource.kaevent.example.methodresolving.event;

import java.util.EventListener;

import org.kasource.kaevent.listener.interfaces.DefaultListenerMethod;
import org.kasource.kaevent.listener.interfaces.KeywordCase;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.listener.interfaces.MethodResolving;




@MethodResolving(MethodResolverType.KEYWORD_SWITCH)
public interface TemperatureChangeEventListener extends EventListener{
	
	@DefaultListenerMethod
	@KeywordCase("UP")
	public void temperatureUp(TemperatureChangeEvent event);
	
	@KeywordCase("DOWN")
	public void temperatureDown(TemperatureChangeEvent event);
}
