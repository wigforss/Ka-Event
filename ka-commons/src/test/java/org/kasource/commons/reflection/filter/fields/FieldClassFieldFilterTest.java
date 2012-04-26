package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FieldClassFieldFilterTest {

    @InjectIntoByType
    @Mock
    private ClassFilter classFilter;
    
    @TestedObject
    private FieldClassFieldFilter filter = new FieldClassFieldFilter(classFilter);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException {
        EasyMock.expect(classFilter.passFilter(int.class)).andReturn(true);
        EasyMockUnitils.replay();
        Field field = MyClass.class.getField("number");
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        EasyMock.expect(classFilter.passFilter(int.class)).andReturn(false);
        EasyMockUnitils.replay();
        Field field = MyClass.class.getField("number");
        assertFalse(filter.passFilter(field));
    }
    
    
  
    
    private static class MyClass {
       
        @SuppressWarnings("unused")
        public int number;
  
    }
}
