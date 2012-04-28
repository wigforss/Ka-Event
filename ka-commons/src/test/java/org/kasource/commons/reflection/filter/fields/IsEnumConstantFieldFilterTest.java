package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class IsEnumConstantFieldFilterTest {
    
    @TestedObject
    private IsEnumConstantFieldFilter filter;
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyEnum.class.getField("FIRST");
      
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        
        assertFalse(filter.passFilter(field));
    }
   
    enum MyEnum{FIRST,SECOND};
    
    private static class MyClass {
        @SuppressWarnings("unused")
        public ElementType elementType = ElementType.ANNOTATION_TYPE;
        
        @SuppressWarnings("unused")
        public int number;
  
    }
}
