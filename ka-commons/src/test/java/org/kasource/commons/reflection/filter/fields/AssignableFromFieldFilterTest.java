package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableFromFieldFilterTest {
    @TestedObject
    private AssignableFromFieldFilter filter = new AssignableFromFieldFilter(List.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("list");
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("nonList");
        assertFalse(filter.passFilter(field));
    }
    
 
    
    private static class MyClass {
      
        @SuppressWarnings("unused")
        public ArrayList<String> list = new ArrayList<String>();
        @SuppressWarnings("unused")
        public int nonList;
    }

}
