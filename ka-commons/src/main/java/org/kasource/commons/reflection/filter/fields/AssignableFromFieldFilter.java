package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class AssignableFromFieldFilter implements FieldFilter {

    /**
     * Interface or super class to test candidates with
     **/
    private Class<?> assignable;
    
    public AssignableFromFieldFilter(Class<?> assignable) {
        this.assignable = assignable;
    }
    
    @Override
    public boolean passFilter(Field field) {
        return assignable.isAssignableFrom(field.getType());
    }

}
