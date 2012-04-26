package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.classes.NegationClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NegationClassFilterTest {
    
    @InjectIntoByType
    @Mock
    private ClassFilter classFilter;
    
    @TestedObject
    private NegationClassFilter filter = new NegationClassFilter(classFilter);
    
    @Test
    public void passTrue() {
        EasyMock.expect(classFilter.passFilter(Integer.class)).andReturn(false);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(Integer.class));
    }
    
    @Test
    public void passFalse() {
        EasyMock.expect(classFilter.passFilter(Integer.class)).andReturn(true);
        EasyMockUnitils.replay();
        assertFalse(filter.passFilter(Integer.class));
    }
}
