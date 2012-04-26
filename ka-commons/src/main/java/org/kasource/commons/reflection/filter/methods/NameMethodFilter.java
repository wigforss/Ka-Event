package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class NameMethodFilter implements MethodFilter {

    private String nameRegExp;
    
    public NameMethodFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }
    
    
    @Override
    public boolean passFilter(Method method) {
        return method.getName().matches(nameRegExp);
    }

}
