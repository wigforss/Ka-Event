package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class IsEnumConstantFieldFilter implements FieldFilter {

    @Override
    public boolean passFilter(Field field) {
       return field.isEnumConstant();
    }

}
