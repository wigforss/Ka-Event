package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NumberOfParametersMethodFilterTest {
    
    @TestedObject
    private NumberOfParametersMethodFilter filter = new NumberOfParametersMethodFilter(1);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getName");
        assertFalse(filter.passFilter(method));
    }
    
    private static class MyClass {
        private String name;
        
        @SuppressWarnings("unused")
        public String getName() {return name;}
        
        @SuppressWarnings("unused")
        public void setName(String name) {this.name = name;}
    }
}
