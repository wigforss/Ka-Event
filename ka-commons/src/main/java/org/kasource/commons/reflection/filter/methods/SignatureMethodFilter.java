package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SignatureMethodFilter implements MethodFilter {

    private Class<?>[] params;
    
    public SignatureMethodFilter(Class<?>... params) {
        this.params = params;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return (Arrays.equals(method.getParameterTypes(), params));
    }

}
