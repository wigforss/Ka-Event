/**
 * 
 */
package org.kasource.kaevent.bean;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class DefaultBeanResolverTest {

    @TestedObject
    private DefaultBeanResolver resolver;
    
    @Test(expected = CouldNotResolveBeanException.class)
    public void getBeanTest() {
        resolver.getBean("test", String.class);
    }
    
    @Test
    public void getBeanFromContextTest() {
    	resolver.addBean("testBean", "Test");
    	assertEquals("Test", resolver.getBean("testBean", String.class));
    }
    
}
