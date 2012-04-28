package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MetaAnnotationClassFilterTest {

    @TestedObject
    private MetaAnnotatedClassFilter filter = new MetaAnnotatedClassFilter(MetaAnnotation.class);
    
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(MyClass.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(MyAnnotation.class));
    }
    
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MetaAnnotation {
        
    }
    
    @MetaAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MyAnnotation {
        
    }
    
    @MyAnnotation
    private static class MyClass {
        
    }
    
}
