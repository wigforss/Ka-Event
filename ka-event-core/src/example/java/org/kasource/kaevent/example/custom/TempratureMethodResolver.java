/**
 * 
 */
package org.kasource.kaevent.example.custom;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.example.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.custom.event.TemperatureChangeListener;

/**
 * @author rikardwigforss
 * 
 */
//CHECKSTYLE:OFF
///CLOVER:OFF
public class TempratureMethodResolver implements MethodResolver<TemperatureChangeEvent> {
    
    private double optimalTemp;

    TempratureMethodResolver(double optimalTemp) {
        this.optimalTemp = optimalTemp;
    }

    public static TempratureMethodResolver getInstance() {
        return new TempratureMethodResolver(22.0);
    }
    
    public static TempratureMethodResolver getInstance(String optimalTemp) {
        return new TempratureMethodResolver(Double.parseDouble(optimalTemp));
    }
    
    
    
    @Override
    public Method resolveMethod(TemperatureChangeEvent event, Object target) {
        if (event.getCurrentTemperature() > optimalTemp) {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeListener.class, "highTemperature",
                    TemperatureChangeEvent.class);
        } else if (event.getCurrentTemperature() > 10) {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeListener.class, "mediumTemperature",
                    TemperatureChangeEvent.class);
        } else {
            return ReflectionUtils.getDeclaredMethod(TemperatureChangeListener.class, "lowTemperature",
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
