package org.kasource.commons.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import org.junit.Test;
import org.kasource.commons.util.reflection.InterfaceUtils;

public class InterfaceUtilsTest {
    @Test
    public void implementsInterfaceTrueTest() {
        assertEquals(true, InterfaceUtils.implementsInterface(new Foo(),EventListener.class));
    }
    
    @Test
    public void implementsInterfaceFalseTest() {
        assertEquals(false, InterfaceUtils.implementsInterface(new Bar(), EventListener.class));
    }
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RuntimeTypeAnnotation {

    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @RuntimeParentAnnotation
    public @interface RuntimeMethodAnnotation {

    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RuntimeMethodOrphanAnnotation {

    }
    
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RuntimeParentAnnotation {

    }
      
    
    
    
    @RuntimeTypeAnnotation
    ///CLOVER:OFF
    private static class Foo implements EventListener{
        @SuppressWarnings("unused")
        @RuntimeMethodAnnotation
        public void run(){}
    }
    
    ///CLOVER:OFF
    private static class Tor{
        @SuppressWarnings("unused")
        @RuntimeMethodOrphanAnnotation
        public void run(){}
        
        @SuppressWarnings("unused")
        public String getName(){
            return "Bar";
        }
    }
    
    @Target(ElementType.TYPE)
    public @interface CompileTypeAnnotation {

    }
    
    @Target(ElementType.METHOD)
    public @interface CompileMethodAnnotation {

    }
    
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface CompileParentAnnotation {

    }
    

    @CompileTypeAnnotation
    ///CLOVER:OFF
    private static class Bar {
        @SuppressWarnings("unused")
        @CompileMethodAnnotation
        public void run(){}
        
        @SuppressWarnings("unused")
        public String getName(){
            return "Bar";
        }
    }
    
    ///CLOVER:OFF
    public static class Buzz{
        
    }
    
    ///CLOVER:OFF
    public static class Fuzz{

        @RuntimeMethodAnnotation
        public void run(){}

        @RuntimeMethodAnnotation
        public String getName(){
            return "Bar";
        }
    }
}
