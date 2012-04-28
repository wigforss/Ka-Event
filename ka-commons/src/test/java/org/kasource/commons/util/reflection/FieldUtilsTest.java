package org.kasource.commons.util.reflection;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;
import org.kasource.commons.reflection.filter.fields.FieldFilterBuilder;

public class FieldUtilsTest {

    @Test
    public void getDeclaredFields() throws SecurityException, NoSuchFieldException {
       Set<Field> fields = FieldUtils.getDeclaredFields(FooClass.class, new FieldFilterBuilder().isProtected().build());
       assertFalse(fields.isEmpty());
       assertEquals(1, fields.size());
       assertEquals(FooClass.class.getDeclaredField("field2"), fields.iterator().next());
    }
    
    @Test
    public void getFields() throws SecurityException, NoSuchFieldException {
       Set<Field> fields = FieldUtils.getFields(FooClass.class, new FieldFilterBuilder().isProtected().build());
       assertFalse(fields.isEmpty());
       assertEquals(2, fields.size());
       assertTrue(fields.contains(BarClass.class.getDeclaredField("field1")));
       assertTrue(fields.contains(FooClass.class.getDeclaredField("field2"))); 
    }
    
    private static class BarClass {
        protected int field1;
    }
    
    private static class FooClass extends BarClass{
        protected String field2;
        public String field3;
    }
    
   
}
