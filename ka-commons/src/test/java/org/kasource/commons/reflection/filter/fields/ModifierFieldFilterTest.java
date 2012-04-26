package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ModifierFieldFilterTest {

    @TestedObject
    private ModifierFieldFilter filter = new ModifierFieldFilter(Modifier.PUBLIC);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("publicList");
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("privateNumber");
        assertFalse(filter.passFilter(field));
    }
    
 
    
    private static class MyClass {
      
        @SuppressWarnings("unused")
        public ArrayList<String> publicList = new ArrayList<String>();
        @SuppressWarnings("unused")
        private int privateNumber;
    }
}
