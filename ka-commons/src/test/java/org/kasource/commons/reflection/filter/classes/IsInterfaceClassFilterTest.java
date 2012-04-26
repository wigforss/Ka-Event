package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EventListener;
import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.IsInterfaceClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class IsInterfaceClassFilterTest {
    
    @TestedObject
    private IsInterfaceClassFilter filter;
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(EventListener.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(EventObject.class));
    }
}
