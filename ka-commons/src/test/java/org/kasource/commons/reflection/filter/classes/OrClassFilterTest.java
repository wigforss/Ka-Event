package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.classes.OrClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class OrClassFilterTest {
    @InjectInto(property = "left")
    @Mock
    private ClassFilter leftFilter;
    
    @InjectInto(property = "right")
    @Mock
    private ClassFilter rightFilter;
    
    @TestedObject
    private OrClassFilter filter = new OrClassFilter(leftFilter, rightFilter);
    
    @Test
    public void passTrue1() {
        EasyMock.expect(leftFilter.passFilter(Integer.class)).andReturn(false);
        EasyMock.expect(rightFilter.passFilter(Integer.class)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(Integer.class));
    }
    
    @Test
    public void passTrue2() {
        EasyMock.expect(leftFilter.passFilter(Integer.class)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(Integer.class));
    }
    
    
    
    @Test
    public void passFalse() {
        EasyMock.expect(leftFilter.passFilter(Integer.class)).andReturn(false);
        EasyMock.expect(rightFilter.passFilter(Integer.class)).andReturn(false);
        EasyMockUnitils.replay();
        assertFalse(filter.passFilter(Integer.class));
    }
}
