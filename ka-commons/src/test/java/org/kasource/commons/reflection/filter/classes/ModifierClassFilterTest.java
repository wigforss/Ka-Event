package org.kasource.commons.reflection.filter.classes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ModifierClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ModifierClassFilterTest {
    
    @TestedObject
    private ModifierClassFilter filter = new ModifierClassFilter(Modifier.PUBLIC);
    
    @Test
    public void passTrue() {
        
        assertTrue(filter.passFilter(String.class));
    }
    
    @Test
    public void passFalse() {
        InjectionUtils.injectInto(Modifier.PRIVATE, filter, "modifier");
        assertFalse(filter.passFilter(Integer.class));
    }
    
   
}
