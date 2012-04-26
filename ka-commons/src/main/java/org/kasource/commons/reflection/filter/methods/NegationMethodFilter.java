package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class NegationMethodFilter implements MethodFilter {

    private MethodFilter filter;
    
    public NegationMethodFilter(MethodFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return !filter.passFilter(method);
    }

}
