package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

import org.kasource.commons.reflection.filter.classes.ClassFilter;

public class ParameterClassMethodFilter implements MethodFilter {

    private int parameterIndex;
    private ClassFilter filter;
    private ClassFilter[] filters;
    
    public ParameterClassMethodFilter(ClassFilter... filters) {
        this.filters = filters;
    }
    
    public ParameterClassMethodFilter(int parameterIndex, ClassFilter filter) {
        this.parameterIndex = parameterIndex;
        this.filter = filter;
    }

    @Override
    public boolean passFilter(Method method) {
        if(filter != null) {
            if((method.getParameterTypes().length - 1) < parameterIndex) {
                return false;
            }
            return filter.passFilter(method.getParameterTypes()[parameterIndex]);
        } else if(filters != null) {
            if(filters.length != method.getParameterTypes().length) {
                return false;
            }
            for(int i = 0; i <  filters.length; ++i) {
                Class<?> paramterType = method.getParameterTypes()[i];
                if(!filters[i].passFilter(paramterType)) {
                    return false;
                }
            }
        }
        return true;
    }
}
