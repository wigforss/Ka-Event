package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MethodFilterListTest {
    @InjectIntoByType
    private MethodFilter[] filters;
    
    @Mock
    private MethodFilter methodFilter;
    
    @Mock
    private MethodFilter methodFilter2;
    
    @TestedObject
    private MethodFilterList filter = new MethodFilterList(filters);
    
    @Test(expected = NullPointerException.class)
    public void nullFilterTest() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        filter.passFilter(method);
    }
    
    @Test
    public void emptpyFilterTest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{}, filter, "filters");
        Method method = MyClass.class.getMethod("noParamters");
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(methodFilter.passFilter(method)).andReturn(true);
        EasyMock.expect(methodFilter2.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFqlse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(methodFilter.passFilter(method)).andReturn(false);
      
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(method));
    }
    
    @Test
    public void passFalseSecondFilterFalse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(methodFilter.passFilter(method)).andReturn(true);
        EasyMock.expect(methodFilter2.passFilter(method)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(method));
    }
    
   
    
    private static class MyClass {       
        @SuppressWarnings("unused")
        public void noParamters() {}
    }
}
