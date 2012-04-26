package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.classes.ClassFilter;

public class FieldClassFieldFilter implements FieldFilter {

    private ClassFilter filter;
    
    public FieldClassFieldFilter(ClassFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return filter.passFilter(field.getType());
    }

}
