package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ReturnTypeMethodFilterTest {
   
    @TestedObject
    private ReturnTypeMethodFilter filter = new ReturnTypeMethodFilter(String.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getName");
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setName", String.class);
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
