package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotatedFieldFilter implements FieldFilter {

 private Class<? extends Annotation> annotation;
    
    public AnnotatedFieldFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return field.isAnnotationPresent(annotation);
    }

}
