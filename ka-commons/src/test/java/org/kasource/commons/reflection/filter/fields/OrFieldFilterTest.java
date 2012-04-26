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
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class OrFieldFilterTest {

    @InjectInto(property = "left")
    @Mock 
    private FieldFilter left;
    
    @InjectInto(property = "right")
    @Mock 
    private FieldFilter right;
    
    @TestedObject
    private OrFieldFilter filter = new OrFieldFilter(left, right);
    
    @Test
    public void passTrueLeft() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        EasyMock.expect(left.passFilter(field)).andReturn(true);
        EasyMockUnitils.replay();
       
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passTrueRight() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        EasyMock.expect(left.passFilter(field)).andReturn(false);
        EasyMock.expect(right.passFilter(field)).andReturn(true);
        EasyMockUnitils.replay();
       
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field = MyClass.class.getField("number");
        EasyMock.expect(left.passFilter(field)).andReturn(false);
        EasyMock.expect(right.passFilter(field)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(field));
    }
   
    private static class MyClass {
       
        @SuppressWarnings("unused")
        public int number;
  
    }
}
