package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.IsArrayClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class IsArrayClassFilterTest {
    
    @TestedObject
    private IsArrayClassFilter filter;
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(Integer[].class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(Integer.class));
    }
}
