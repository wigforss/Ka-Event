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
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NegationFieldFilterTest {
    
    @InjectIntoByType
    @Mock 
    private FieldFilter fieldFilter;
    
    @TestedObject
    private NegationFieldFilter filter = new NegationFieldFilter(fieldFilter);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        EasyMock.expect(fieldFilter.passFilter(field)).andReturn(false);
        EasyMockUnitils.replay();
       
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        EasyMock.expect(fieldFilter.passFilter(field)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(field));
    }
   
    private static class MyClass {
       
        @SuppressWarnings("unused")
        public int number;
  
    }
}
