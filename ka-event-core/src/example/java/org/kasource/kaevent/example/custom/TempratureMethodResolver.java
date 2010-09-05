/**
 * 
 */
package org.kasource.kaevent.example.custom;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.example.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.custom.event.TemperatureChangeEventListener;

/**
 * @author rikardwigforss
 * 
 */
///CLOVER:OFF
public class TempratureMethodResolver implements MethodResolver<TemperatureChangeEvent> {
    
    public static TempratureMethodResolver getInstance(){
        return new TempratureMethodResolver(22.0);
    }
    
    public static TempratureMethodResolver getInstance(String optimalTemp){
        return new TempratureMethodResolver(Double.parseDouble(optimalTemp));
    }
    
    private double optimalTemp;

    TempratureMethodResolver(double optimalTemp) {
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
