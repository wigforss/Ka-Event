package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MetaAnnotatedFieldFilter implements FieldFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedFieldFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
