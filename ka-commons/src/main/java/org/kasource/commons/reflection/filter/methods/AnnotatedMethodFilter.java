package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotatedMethodFilter implements MethodFilter {

    private Class<? extends Annotation> annotation;
    
    public AnnotatedMethodFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    
    public boolean passFilter(Method method) {
        return method.isAnnotationPresent(annotation);
    }
    
}
