package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ReturnTypeAssignableFromMethodFilterTest {
   
    @TestedObject
    private ReturnTypeAssignableFromMethodFilter filter = new ReturnTypeAssignableFromMethodFilter(List.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("getNames");
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("setNames", ArrayList.class);
        assertFalse(filter.passFilter(method));
    }
    
    private static class MyClass {
        private ArrayList<String> names;
        
        @SuppressWarnings("unused")
        public ArrayList<String> getNames() {return names;}
        
        @SuppressWarnings("unused")
        public void setNames(ArrayList<String> names) {this.names = names;}
    }
    
}
