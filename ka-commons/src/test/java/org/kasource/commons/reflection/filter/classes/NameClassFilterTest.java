package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NameClassFilterTest {

    @TestedObject
    private NameClassFilter filter = new NameClassFilter("filter");
    
    
    @Test
    public void passTrue() {
        InjectionUtils.injectInto("java\\.lang\\..*", filter, "nameRegExp");
        assertTrue(filter.passFilter(String.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(Integer.class));
    }
    
    
    
}
