/**
 * 
 */
package org.kasource.kaevent.example.guice.custom;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEventListener;

/**
 * @author rikardwigforss
 * 
 */
///CLOVER:OFF
public class TempratureMethodResolver implements MethodResolver<TemperatureChangeEvent> {
    
    
    
    private double optimalTemp;

    public TempratureMethodResolver(double optimalTemp) {
        this.optimalTemp = optimalTemp;
    }

    @Override
    public Method resolveMethod(TemperatureChangeEvent event) {
        if (event.getCurrentTemperature() > optimalTemp) {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeEventListener.class, "highTemperature",
                    TemperatureChangeEvent.class);
        } else if (event.getCurrentTemperature() > 10) {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeEventListener.class, "mediumTemperature",
                    TemperatureChangeEvent.class);
        } else {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeEventListener.class, "lowTemperature",
                    TemperatureChangeEvent.class);
        }
    }

    /**
     * @return the optimalTemp
     */
    public double getOptimalTemp() {
        return optimalTemp;
    }

    /**
     * @param optimalTemp
     *            the optimalTemp to set
     */
    public void setOptimalTemp(double optimalTemp) {
        this.optimalTemp = optimalTemp;
    }

}
