package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;



public class MethodFilterList implements MethodFilter {
private MethodFilter[] filters;
    
    public MethodFilterList(MethodFilter... filters) {
        this.filters = filters;
    }
    
    @Override
    public boolean passFilter(Method method) {
        for(MethodFilter filter : filters) {
            if(!filter.passFilter(method)) {
                return false;
            }
        }
        return true;
    }
}
