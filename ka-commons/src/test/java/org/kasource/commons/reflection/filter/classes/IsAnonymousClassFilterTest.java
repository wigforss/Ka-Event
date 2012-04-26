package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EventListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.IsAnonymousClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class IsAnonymousClassFilterTest {
    
    @TestedObject
    private IsAnonymousClassFilter filter;
    
    @Test
    public void passTrue() {
        EventListener listener = new EventListener() {
        };
        assertTrue(filter.passFilter(listener.getClass()));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(Integer.class));
    }
}
