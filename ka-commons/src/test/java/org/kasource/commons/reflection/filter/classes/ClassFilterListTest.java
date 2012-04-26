package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.classes.ClassFilterList;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ClassFilterListTest {

    @Mock
    private ClassFilter classFilter1;
    
    @Mock
    private ClassFilter classFilter2;
    
    private ClassFilter[] filters;
    
    @TestedObject
    private ClassFilterList filter = new ClassFilterList(filters);
    
    @Test (expected = NullPointerException.class)
    public void testNullFilter() {
        filter.passFilter(Integer.class);
    }
    
    @Test
    public void testEmptyFilter() {
        ClassFilter[] filterArray = {};
        InjectionUtils.injectInto(filterArray, filter, "filters");
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(Integer.class));
    }
    
    @Test
    public void testTwoFilterSuccessFilter() {
        ClassFilter[] filterArray = {classFilter1, classFilter2};
        InjectionUtils.injectInto(filterArray, filter, "filters");
        EasyMock.expect(classFilter1.passFilter(Integer.class)).andReturn(true);
        EasyMock.expect(classFilter2.passFilter(Integer.class)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(Integer.class));
    }
    
    @Test
    public void testTwoFilterNonPassFilter() {
        ClassFilter[] filterArray = {classFilter1, classFilter2};
        InjectionUtils.injectInto(filterArray, filter, "filters");
        EasyMock.expect(classFilter1.passFilter(Integer.class)).andReturn(true);
        EasyMock.expect(classFilter2.passFilter(Integer.class)).andReturn(false);
        EasyMockUnitils.replay();
        assertFalse(filter.passFilter(Integer.class));
    }
}
