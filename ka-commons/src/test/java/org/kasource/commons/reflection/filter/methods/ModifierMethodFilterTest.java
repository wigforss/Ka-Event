package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ModifierMethodFilterTest {

    @TestedObject
    private ModifierMethodFilter filter = new ModifierMethodFilter(Modifier.PUBLIC);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("publicMethod");
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getDeclaredMethod("privateMethod");
        assertFalse(filter.passFilter(method));
    }
    
    private static class MyClass {
        @SuppressWarnings("unused")
        public void publicMethod() {}
        
        @SuppressWarnings("unused")
        private void privateMethod() {}
    }
    
}
