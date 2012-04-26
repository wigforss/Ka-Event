package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class ModifierFieldFilter implements FieldFilter {
    
    private int modifier;
    
    public ModifierFieldFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Field field) {
        return (field.getModifiers() & modifier) > 0;
    }

}
