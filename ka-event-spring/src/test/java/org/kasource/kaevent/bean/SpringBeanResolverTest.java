package org.kasource.kaevent.bean;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.kasource.kaevent.bean.SpringBeanResolver;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;


/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringBeanResolverTest {
	
	@Mock
	@InjectIntoByType
	private ApplicationContext applicationContext;
	
	@TestedObject
    private SpringBeanResolver springBeanResolver;
    
    
    @Test
    public void getBeanTest() {
    	Object bean = new Object();
        EasyMock.expect(applicationContext.getBean("test")).andReturn(bean);
        EasyMockUnitils.replay();
        assertEquals(bean,  springBeanResolver.getBean("test"));
    }
    
    @Test(expected=CouldNotResolveBeanException.class)
    public void getBeanExceptionTest() {
        EasyMock.expect(applicationContext.getBean("test")).andThrow(new NoSuchBeanDefinitionException(""));
        EasyMockUnitils.replay();
        springBeanResolver.getBean("test");
    }
}
