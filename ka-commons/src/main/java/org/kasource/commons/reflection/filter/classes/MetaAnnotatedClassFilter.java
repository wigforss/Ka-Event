package org.kasource.commons.reflection.filter.classes;

import java.lang.annotation.Annotation;

public class MetaAnnotatedClassFilter implements ClassFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedClassFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
