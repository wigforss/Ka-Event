package org.kasource.kaevent.event.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventObject;

import javax.swing.event.ChangeEvent;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AnnotatedMethodMethodResolverTest {

    @TestedObject
    private AnnotatedMethodMethodResolver methodResolver = new AnnotatedMethodMethodResolver(OnMyEvent.class, null);
    
    @Mock
    private EventObject event;
    
    @Mock
    private ChangeEvent changeEvent;
    
    @Mock
    private Object target;
    
    @Mock
    private MethodResolver<EventObject> fallback;
    
  
    
    @Test
    public void resolveTooFewEventMethodParameters() {
        MyListener1 listener = new MyListener1();
        assertNull(methodResolver.resolveMethod(event, listener));
    }
    
    
    @Test
    public void resolveWrongTypeOfParameter() {
        MyListener2 listener = new MyListener2();
        assertNull(methodResolver.resolveMethod(event, listener));
    }
    
    @Test
    public void resolveTooManyMethodParameters() {
        MyListener3 listener = new MyListener3();
        assertNull(methodResolver.resolveMethod(event, listener));
    }
    
    @Test
    public void resolveEventMethod() throws SecurityException, NoSuchMethodException {
        MyListener4 listener = new MyListener4();
        Method method = methodResolver.resolveMethod(event, listener);
        assertNotNull(method);
        assertEquals(MyListener4.class.getDeclaredMethod("eventMethod", EventObject.class), method);
    }
    
    @Test
    public void resolveEventMethodMultipleMethods() throws SecurityException, NoSuchMethodException {
        MyListener5 listener = new MyListener5();
        Method method = methodResolver.resolveMethod(event, listener);
        assertNotNull(method);
        assertEquals(MyListener5.class.getDeclaredMethod("eventMethod", EventObject.class), method);
    }
    
    @Test
    public void resolveEventMethodSuperClass() throws SecurityException, NoSuchMethodException {
        MyListener4 listener = new MyListener4();
        Method method = methodResolver.resolveMethod(changeEvent, listener);
        assertNotNull(method);
        assertEquals(MyListener4.class.getDeclaredMethod("eventMethod", EventObject.class), method);
    }
    
    
    @Test
    public void fallbackMethodResolverTest() throws SecurityException, NoSuchMethodException {
        MyListener1 listener = new MyListener1();
        Method fallbackMethod = MyListener4.class.getDeclaredMethod("eventMethod", EventObject.class);
        InjectionUtils.injectInto(fallback, methodResolver, "fallbackMethodResolver");
        EasyMock.expect(fallback.resolveMethod(event, listener)).andReturn(fallbackMethod);
        EasyMockUnitils.replay();
        Method method = methodResolver.resolveMethod(event, listener);
        assertNotNull(method);
        assertEquals(fallbackMethod, method);
    }
    
    
    @Test
    public void targetAnnotationTest() {
        assertEquals(OnMyEvent.class, methodResolver.getTargetAnnotation());
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface OnMyEvent{
        
    }
    
    private static class MyListener1 {
        
        @OnMyEvent
        public void tooFewParameters() {
            
        }     
        
    }
    
    private static class MyListener2 {
        
        @OnMyEvent
        public void wrongParameterType(ChangeEvent event) {
            
        }
        
    }
    
    private static class MyListener3 {
          
        @OnMyEvent
        public void tooManyParameters(EventObject event, int count) {
            
        }
        
    }
    
    private static class MyListener4 {
        
        @OnMyEvent
        public void eventMethod(EventObject event) {
            
        }
        
    }
    
    private static class MyListener5 {
        @OnMyEvent
        public void tooFewParameters() {
            
        }    
        
        @OnMyEvent
        public void wrongParameterType(ChangeEvent event) {
            
        }
        
        @OnMyEvent
        public void tooManyParameters(EventObject event, int count) {
            
        }
        
        @OnMyEvent
        public void eventMethod(EventObject event) {
            
        }
        
    }
}
