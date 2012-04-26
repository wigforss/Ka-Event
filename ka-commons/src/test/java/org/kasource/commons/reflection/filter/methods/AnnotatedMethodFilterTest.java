package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AnnotatedMethodFilterTest {

    @TestedObject
    private AnnotatedMethodFilter filter = new AnnotatedMethodFilter(MyAnnotation.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("annotated");
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("notAnnotated");
        assertFalse(filter.passFilter(method));
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        
    }
    
    private static class MyClass {
        
        @MyAnnotation
        public void annotated() {
            
        }
        
        public void notAnnotated() {
            
        }
    }
}
