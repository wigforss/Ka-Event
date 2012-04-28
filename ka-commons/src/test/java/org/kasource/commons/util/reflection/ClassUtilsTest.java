package org.kasource.commons.util.reflection;

import java.util.EventListener;
import java.util.List;



import org.junit.Test;

public class ClassUtilsTest {

    
    @Test
    public void getClassTest() {
       Class<? extends List> list =  ClassUtils.getClass("java.util.ArrayList", List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getClassNotAssingableTest() {
       Class<? extends List> list =  ClassUtils.getClass("java.lang.Integer", List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getClassNotFoundTest() {
       Class<? extends List> list =  ClassUtils.getClass("org.myorg.MyClass", List.class);
    }
    
    @Test
    public void getInterfaceClassTest() {
       
       Class<? extends EventListener> listener =  ClassUtils.getClass("javax.swing.event.ChangeListener", EventListener.class);
    }
    
 
}
