package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class ModifierMethodFilter implements MethodFilter {

    private int modifier;
    
    public ModifierMethodFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Method method) {
       return (method.getModifiers() & modifier) > 0;
       
    }

}
