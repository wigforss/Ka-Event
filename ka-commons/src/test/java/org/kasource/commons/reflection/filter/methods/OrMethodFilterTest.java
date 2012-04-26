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
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class OrMethodFilterTest {

    @InjectInto(property = "left")
    @Mock
    private MethodFilter left;
    
    @InjectInto(property = "right")
    @Mock
    private MethodFilter right;
    
    @Mock
    private MethodFilter other;
    
    @Mock
    private MethodFilter other2;
    
    @TestedObject
    private OrMethodFilter filter = new OrMethodFilter(left, right);
    
    @Test
    public void passTrueRight() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(left.passFilter(method)).andReturn(false);
        EasyMock.expect(right.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passTrueLeft() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(left.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passTrueTheRest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{other}, filter, "rest");
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(left.passFilter(method)).andReturn(false);
        EasyMock.expect(right.passFilter(method)).andReturn(false);
        EasyMock.expect(other.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passTrueTheRest2() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new MethodFilter[]{other, other2}, filter, "rest");
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(left.passFilter(method)).andReturn(false);
        EasyMock.expect(right.passFilter(method)).andReturn(false);
        EasyMock.expect(other.passFilter(method)).andReturn(false);
        EasyMock.expect(other2.passFilter(method)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(method));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
        Method method = MyClass.class.getMethod("noParamters");
        EasyMock.expect(left.passFilter(method)).andReturn(false);
        EasyMock.expect(right.passFilter(method)).andReturn(false);
        EasyMockUnitils.replay();
       
        assertFalse(filter.passFilter(method));
    }
    
    private static class MyClass {       
        @SuppressWarnings("unused")
        public void noParamters() {}
    }
}
