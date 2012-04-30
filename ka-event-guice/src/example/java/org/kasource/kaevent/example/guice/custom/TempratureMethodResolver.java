package org.kasource.kaevent.example.guice.custom;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.MethodFilterBuilder;
import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeListener;

/**
 * @author rikardwigforss
 * 
 */
//CHECKSTYLE:OFF
///CLOVER:OFF
public class TempratureMethodResolver implements MethodResolver<TemperatureChangeEvent> {
    
    private double optimalTemp;

    public TempratureMethodResolver(double optimalTemp) {
        this.optimalTemp = optimalTemp;
    }

    @Override
    public Method resolveMethod(TemperatureChangeEvent event, Object target) {
        if (event.getCurrentTemperature() > optimalTemp) {
            return MethodUtils.getDeclaredMethods(TemperatureChangeListener.class, 
                        new MethodFilterBuilder().name("highTemperature").hasSignature(TemperatureChangeEvent.class).build()).iterator().next();
                   
        } else if (event.getCurrentTemperature() > 10) {
            return MethodUtils.getDeclaredMethods(TemperatureChangeListener.class, 
                        new MethodFilterBuilder().name("mediumTemperature").hasSignature(TemperatureChangeEvent.class).build()).iterator().next();
        } else {
            return MethodUtils.getDeclaredMethods(TemperatureChangeListener.class, 
                        new MethodFilterBuilder().name("lowTemperature").hasSignature(TemperatureChangeEvent.class).build()).iterator().next();
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
