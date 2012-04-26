package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InheritlyAnnotatedMethodFilter implements MethodFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public InheritlyAnnotatedMethodFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
