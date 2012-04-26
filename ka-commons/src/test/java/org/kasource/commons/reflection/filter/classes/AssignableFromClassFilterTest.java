package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.AssignableFromClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableFromClassFilterTest {

    
    
    @TestedObject
    private AssignableFromClassFilter filter = new AssignableFromClassFilter(List.class);
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(ArrayList.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(HashSet.class));
    }
}
