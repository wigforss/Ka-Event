package org.kasource.commons.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import org.junit.Test;
import org.kasource.commons.reflection.filter.classes.ClassFilterBuilder;
import org.kasource.commons.util.reflection.InterfaceUtils;

public class InterfaceUtilsTest {
    @Test
    public void implementsInterfaceTrueTest() {
        assertEquals(true, InterfaceUtils.implementsInterface(Foo.class, EventListener.class));
    }
    
    @Test
    public void implementsInterfaceFalseTest() {
        assertEquals(false, InterfaceUtils.implementsInterface(Bar.class, EventListener.class));
    }
    
    @Test
    public void getInterfaceExtending() {
        assertEquals(EventListener.class, InterfaceUtils.getDeclaredInterfaces(Foo.class, new ClassFilterBuilder().extendsType(EventListener.class).build()).iterator().next());
    }
    
    @Test
    public void getInterfaceExtendingFromSuperClass() {
        assertEquals(EventListener.class, InterfaceUtils.getInterfaces(Buzz.class, new ClassFilterBuilder().extendsType(EventListener.class).build()).iterator().next());
    }
    
    @Test
    public void getInterfaceExtendingNothingFound() {
        assertTrue(InterfaceUtils.getDeclaredInterfaces(Bar.class, new ClassFilterBuilder().extendsType(EventListener.class).build()).isEmpty());
    }
    

    
    ///CLOVER:OFF
    private static class Foo implements EventListener{
        @SuppressWarnings("unused")
        public void run(){}
    }
    
   
    
    
    

    ///CLOVER:OFF
    private static class Bar {
        @SuppressWarnings("unused")
        public void run(){}
        
        @SuppressWarnings("unused")
        public String getName(){
            return "Bar";
        }
    }
    
    ///CLOVER:OFF
    public static class Buzz extends Foo{
        
    }
    
   
}
