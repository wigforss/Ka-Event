package org.kasource.commons.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Set;

import org.junit.Test;
import org.kasource.commons.reflection.filter.methods.AnnotatedMethodFilter;
import org.kasource.commons.reflection.filter.methods.SignatureMethodFilter;
import org.kasource.commons.util.reflection.MethodUtils;


public class MethodUtilsTest {
    @Test
    public void getMethodCountOneTest() {
        assertEquals(1, MethodUtils.getDeclaredMethodCount(Foo.class));
    }
    
    @Test
    public void getMethodCountZeroTest() {
        assertEquals(0, MethodUtils.getDeclaredMethodCount(Buzz.class));
    }
    
    @Test
    public void getMethodTest() {
        assertEquals("run", MethodUtils.getDeclaredMethod(Bar.class,"run").getName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getMethodNoSuchMethodTest() {
        MethodUtils.getDeclaredMethod(Buzz.class,"run");
    }
    
   
    
    @Test
    public void getMethodsTestTwo() {
        Set<Method> methods = MethodUtils.getDeclaredMethods(Bar.class, new SignatureMethodFilter());
        assertEquals(2, methods.size());
    }
    
    @Test
    public void getAnnotatedMethodTest(){
        Method method = MethodUtils.getDeclaredMethods(Foo.class, new AnnotatedMethodFilter(RuntimeMethodAnnotation.class)).iterator().next();
        assertEquals("run", method.getName());
    }
    
    @Test
    public void getAnnotatedMethodNotRuntimeAnnotationTest(){
        Method method = MethodUtils.getDeclaredMethods(Bar.class, new AnnotatedMethodFilter(CompileMethodAnnotation.class)).iterator().next();
        assertNull(method);
    }
    
    @Test
    public void getAnnotatedMethodNoMethodsTest(){
        Method method = MethodUtils.getDeclaredMethods(Buzz.class, new AnnotatedMethodFilter(RuntimeMethodAnnotation.class)).iterator().next();
        assertNull(method);
    }
    
    
    
   
    
    @Test
    public void hasMethodVoidReturnTypeTrueTest() {
       assertTrue( MethodUtils.hasMethodVoidReturnType( MethodUtils.getDeclaredMethod(Fuzz.class, "run")) );
    }
    
    @Test
    public void hasMethodVoidReturnTypeFalseTest() {
       assertFalse( MethodUtils.hasMethodVoidReturnType( MethodUtils.getDeclaredMethod(Fuzz.class, "getName")) );
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
