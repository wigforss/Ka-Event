package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AnnotatedFieldFilterTest {

    @TestedObject
    private AnnotatedFieldFilter filter = new AnnotatedFieldFilter(MyAnnotation.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("annotated");
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("notAnnotated");
        assertFalse(filter.passFilter(field));
    }
    
    
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        
    }
    
    private static class MyClass {
        @SuppressWarnings("unused")
        @MyAnnotation
        public int annotated;
        @SuppressWarnings("unused")
        public int notAnnotated;
    }
}
