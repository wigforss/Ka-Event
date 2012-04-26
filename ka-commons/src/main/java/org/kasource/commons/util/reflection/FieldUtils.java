package org.kasource.commons.util.reflection;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.filter.fields.FieldFilter;

public class FieldUtils {
    
    private FieldUtils() {}
    
    public static Set<Field> getDeclaredFields(Class<?> clazz, FieldFilter filter) {
        Set<Field> fields = new HashSet<Field>();
        Field[] allFields = clazz.getDeclaredFields();
        for(Field field : allFields) {
            fields.add(field);
        }
        return fields;
    }
    
    public static Set<Field> getFields(Class<?> clazz, FieldFilter filter) {
        Set<Field> fields = getDeclaredFields(clazz, filter);
        while((clazz = clazz.getSuperclass()) != null) {    
            fields.addAll(getDeclaredFields(clazz, filter));
        }
        return fields;
    }
    
}
