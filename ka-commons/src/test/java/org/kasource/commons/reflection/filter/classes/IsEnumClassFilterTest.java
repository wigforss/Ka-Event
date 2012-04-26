package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.IsEnumClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class IsEnumClassFilterTest {
    
    @TestedObject
    private IsEnumClassFilter filter;
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(ElementType.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(Integer.class));
    }
}
