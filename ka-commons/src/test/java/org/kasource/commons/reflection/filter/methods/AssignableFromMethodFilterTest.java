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
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableFromMethodFilterTest {

    @TestedObject
    private AssignableFromMethodFilter filter = new AssignableFromMethodFilter(0, List.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("methodWithListParameter", ArrayList.class);
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalseNoSuchParameterIndex() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParameter");
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("methodWithIntParameter", Integer.class);
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passTrueTwoParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Method method = MyClass.class.getMethod("methodWithListAndIntegerParameter", ArrayList.class, Integer.class);
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalseTooFewParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Method method = MyClass.class.getMethod("methodTooManayParameter", ArrayList.class, Integer.class, List.class);
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passFalseTooManyParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Method method = MyClass.class.getMethod("methodWithListParameter", ArrayList.class);
        assertFalse(filter.passFilter(method));
    }
    
    
    
    private static class MyClass {
        
       
        @SuppressWarnings("unused")
        public void methodWithListParameter(ArrayList<String> list) {}
        
        @SuppressWarnings("unused")
        public void methodWithIntParameter(Integer number) {}
        
        @SuppressWarnings("unused")
        public void methodWithListAndIntegerParameter(ArrayList<String> list, Integer number) {}
        
        @SuppressWarnings("unused")
        public void methodTooManayParameter(ArrayList<String> list, Integer number, List<String> list2) {}
        
        @SuppressWarnings("unused")
        public void noParameter() {}
    }
}
