package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class NumberOfParametersMethodFilter implements MethodFilter {
    private int numberOfParameters;
    
    public NumberOfParametersMethodFilter(int numberOfParameters) {
        this.numberOfParameters = numberOfParameters;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return method.getParameterTypes().length == numberOfParameters;
    }

}
