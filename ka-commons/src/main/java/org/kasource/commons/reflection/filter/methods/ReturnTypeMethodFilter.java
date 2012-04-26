package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class ReturnTypeMethodFilter implements MethodFilter {

    private Class<?> returnType;
    
    public ReturnTypeMethodFilter(Class<?> returnType) {
        this.returnType = returnType;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return method.getReturnType().equals(returnType);
    }

}
