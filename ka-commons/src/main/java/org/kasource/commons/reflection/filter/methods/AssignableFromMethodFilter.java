package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class AssignableFromMethodFilter implements MethodFilter {

    private int paramterIndex;
    private Class<?> assignbleFromClass;
    private Class<?>[] assignbleFrom;
    
    public AssignableFromMethodFilter(int paramterIndex, Class<?> assignbleFromClass) {
        this.paramterIndex = paramterIndex;
        this.assignbleFromClass = assignbleFromClass;
    }
    
    public AssignableFromMethodFilter(Class<?>... assignbleFrom) {
      this.assignbleFrom = assignbleFrom;
    }
    
    @Override
    public boolean passFilter(Method method) {
        if(assignbleFromClass != null) {
            if((method.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return assignbleFromClass.isAssignableFrom(method.getParameterTypes()[paramterIndex]);
        } else if(assignbleFrom.length == method.getParameterTypes().length) {
            for(int i = 0; i < assignbleFrom.length; i++) {
               if(!assignbleFrom[i].isAssignableFrom(method.getParameterTypes()[i])) {
                   return false;
               }
            }
            return true;
        }
        return false;
    }

}
