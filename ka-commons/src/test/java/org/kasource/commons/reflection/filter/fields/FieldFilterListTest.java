package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FieldFilterListTest {
    
   
    private FieldFilter[] filters;
    
    @Mock
    private FieldFilter fieldFilter;
    
    @Mock
    private FieldFilter fieldFilter2;
    
    @TestedObject
    private FieldFilterList filter = new FieldFilterList(filters);
    
    @Test(expected = NullPointerException.class)
    public void nullFiltersTest() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        assertFalse(filter.passFilter(field));
    }
    
    @Test
    public void passTrueEmptyLisst() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(new FieldFilter[]{}, filter, "filters");
        EasyMockUnitils.replay();
        Field field = MyClass.class.getField("number");
        assertTrue(filter.passFilter(field));
    }
    
    
    @Test
    public void passTrueTwoFiltersFirstTrue() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(new FieldFilter[]{fieldFilter, fieldFilter2}, filter, "filters");
        Field field = MyClass.class.getField("number");
        EasyMock.expect(fieldFilter.passFilter(field)).andReturn(true);
        EasyMock.expect(fieldFilter2.passFilter(field)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalseTwoFiltersFirstTrue() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(new FieldFilter[]{fieldFilter, fieldFilter2}, filter, "filters");
        Field field = MyClass.class.getField("number");
        EasyMock.expect(fieldFilter.passFilter(field)).andReturn(true);
        EasyMock.expect(fieldFilter2.passFilter(field)).andReturn(false); 
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(field));
    }
    
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        InjectionUtils.injectInto(new FieldFilter[]{fieldFilter, fieldFilter2}, filter, "filters");
        Field field = MyClass.class.getField("number");
        EasyMock.expect(fieldFilter.passFilter(field)).andReturn(false);
       
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(field));
    }
    
    
  
    
    private static class MyClass {
       
        @SuppressWarnings("unused")
        public int number;
  
    }
}
