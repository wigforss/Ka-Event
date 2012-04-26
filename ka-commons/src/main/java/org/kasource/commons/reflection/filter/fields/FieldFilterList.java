package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class FieldFilterList implements FieldFilter {

    private FieldFilter[] filters;
    
    public FieldFilterList(FieldFilter... filters) {
        this.filters = filters;
    }
    
    
    @Override
    public boolean passFilter(Field field) {
        for (FieldFilter filter : filters) {
            if (!filter.passFilter(field)) {
                return false;
            }
        }
        return true;
    }

}
