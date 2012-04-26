package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class NameFieldFilter implements FieldFilter {

    private String nameRegExp;
    
    public NameFieldFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }

    @Override
    public boolean passFilter(Field field) {
       return field.getName().matches(nameRegExp);
    }
    
    
}
