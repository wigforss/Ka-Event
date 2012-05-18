package org.kasource.kaevent.event.method;

import static org.easymock.classextension.EasyMock.*;

import java.util.EventObject;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.mock.annotation.Dummy;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MethodResolverFactoryTest {
  
    @Mock
    private BeanResolver beanResolver;
    
    @Dummy
    private MethodResolver<EventObject> methodResolver;
    
    @Test
    public void methodResolverFromBeanTest() {
        expect(beanResolver.getBean("testName", MethodResolver.class)).andReturn(methodResolver);
        EasyMockUnitils.replay();
        assertEquals(methodResolver, MethodResolverFactory.getFromBean(beanResolver, "testName"));
    }
    
    @Test(expected = CouldNotResolveBeanException.class)
    public void methodResolverFromNonExsistingBeanTest() {
        expect(beanResolver.getBean("testName", MethodResolver.class)).andThrow(new CouldNotResolveBeanException("Test"));
        EasyMockUnitils.replay();
        MethodResolverFactory.getFromBean(beanResolver, "testName");
    }
}
