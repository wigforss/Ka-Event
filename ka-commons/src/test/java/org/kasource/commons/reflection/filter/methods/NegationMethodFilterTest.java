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

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NegationMethodFilterTest {

    @InjectIntoByType
    @Mock
    private MethodFilter methodFilter;
    
    @TestedObject
    private NegationMethodFilter filter = new NegationMethodFilter(methodFilter);
    
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(methodFilter.passFilter(method)).andReturn(false);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(methodFilter.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
       
        assertFalse(filter.passFilter(method));
    }
    
    private static class MyClass {       
        @SuppressWarnings("unused")
        public void noParamters() {}
    }
}
