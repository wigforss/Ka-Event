package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ParamterClassMethodFilterTest {

    @InjectIntoByType
    @Mock
    private ClassFilter classFilter;
    
    @Mock
    private ClassFilter classFilter2;
    
    @TestedObject
    private ParameterClassMethodFilter filter = new ParameterClassMethodFilter(0, classFilter);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        EasyMock.expect(classFilter.passFilter(int.class)).andReturn(true);
        EasyMockUnitils.replay();
        Method method = MyClass.class.getMethod("oneParamter", int.class);
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFqlse() throws SecurityException, NoSuchMethodException {
        EasyMock.expect(classFilter.passFilter(int.class)).andReturn(false);
        EasyMockUnitils.replay();
        Method method = MyClass.class.getMethod("oneParamter", int.class);
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passFalseTooFewParameters() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passTrueTwoParameters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{classFilter, classFilter2}, filter, "filters");
        EasyMock.expect(classFilter.passFilter(int.class)).andReturn(true);
        EasyMock.expect(classFilter2.passFilter(String.class)).andReturn(true);
        EasyMockUnitils.replay();
        Method method = MyClass.class.getMethod("twoParamters", int.class, String.class);
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalseTooManyParameters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{classFilter, classFilter2}, filter, "filters");
        Method method = MyClass.class.getMethod("threeParamters", int.class, int.class, int.class);
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passTrueEmptyFilters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{}, filter, "filters");
        Method method = MyClass.class.getMethod("noParamters");
        assertTrue(filter.passFilter(method));
    }
    
    private static class MyClass {
        
        @SuppressWarnings("unused")
        public void oneParamter(int number) {}
        
        @SuppressWarnings("unused")
        public void twoParamters(int number, String string) {}
        
        @SuppressWarnings("unused")
        public void threeParamters(int number1, int number2, int number3) {}
        
        @SuppressWarnings("unused")
        public void noParamters() {}
    }
}
