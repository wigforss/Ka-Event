package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class NegationFieldFilter implements FieldFilter {

    private FieldFilter filter;
    
    public NegationFieldFilter(FieldFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return !filter.passFilter(field);
    }

}
