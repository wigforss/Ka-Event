package org.kasource.kaevent.spring.bean;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;



/**
 * @author rikardwigforss
 *
 */
public class SpringBeanResolverTest {
    private SpringBeanResolver springBeanResolver;
    
    
    @Test
    public void getBeanTest() {
        SpringBeanResolver resolver = new SpringBeanResolver();
        ApplicationContext applicationContext = EasyMock.createMock(ApplicationContext.class);
        resolver.setApplicationContext(applicationContext);
        EasyMock.expect(applicationContext.getBean("test")).andReturn(new Object());
        EasyMock.replay(applicationContext);
        resolver.getBean("test");
        EasyMock.verify(applicationContext);
    }
    
    @Test(expected=CouldNotResolveBeanException.class)
    public void getBeanExceptionTest() {
        SpringBeanResolver resolver = new SpringBeanResolver();
        ApplicationContext applicationContext = EasyMock.createMock(ApplicationContext.class);
        resolver.setApplicationContext(applicationContext);
        EasyMock.expect(applicationContext.getBean("test")).andThrow(new NoSuchBeanDefinitionException(""));
        EasyMock.replay(applicationContext);
        resolver.getBean("test");
    }
}
