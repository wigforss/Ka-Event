package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public interface FieldFilter {
    public boolean passFilter(Field field);
}
