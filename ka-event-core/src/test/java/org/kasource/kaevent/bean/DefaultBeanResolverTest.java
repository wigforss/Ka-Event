/**
 * 
 */
package org.kasource.kaevent.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class DefaultBeanResolverTest {

    @TestedObject
    private DefaultBeanResolver resolver;
    
    @Test(expected=CouldNotResolveBeanException.class)
    public void getBeanTest() {
        resolver.getBean("test");
    }
    
}