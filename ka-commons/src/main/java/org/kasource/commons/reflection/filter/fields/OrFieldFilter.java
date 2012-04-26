package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class OrFieldFilter implements FieldFilter {

    private FieldFilter left;
    private FieldFilter right;
    
    public OrFieldFilter(FieldFilter left, FieldFilter right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean passFilter(Field field) {
      return left.passFilter(field) || right.passFilter(field);
    }

}
