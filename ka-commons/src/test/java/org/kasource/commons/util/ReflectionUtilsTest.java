/**
 * 
 */
package org.kasource.commons.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author rikardwigforss
 *
 */
public class ReflectionUtilsTest {

    
    
    @Test
    public void hasRuntimeAnnotationTest() {
        assertEquals(true, ReflectionUtils.hasAnnotation(Foo.class, RuntimeTypeAnnotation.class));     
    }
    
    @Test
    public void hasClassAnnotationTest() {
        assertEquals(false, ReflectionUtils.hasAnnotation(Bar.class, CompileTypeAnnotation.class));      
    }
    
   
    
    @Test
    public void hasAnnotatatedMethodTrueTest() {
        assertEquals(true, ReflectionUtils.hasAnnotatatedMethod(Foo.class, RuntimeMethodAnnotation.class));
    }
    
    @Test
    public void hasAnnotatatedMethodNoSuchAnnotationTest() {
        assertEquals(false, ReflectionUtils.hasAnnotatatedMethod(Foo.class, RuntimeTypeAnnotation.class));
    }
    
    @Test
    public void hasAnnotatatedMethodNotRuntimeTest() {
        assertEquals(false, ReflectionUtils.hasAnnotatatedMethod(Bar.class, CompileMethodAnnotation.class));
    }
    
    @Test
    public void hasAnnotatatedMethodNoMethodsTest() {
        assertEquals(false, ReflectionUtils.hasAnnotatatedMethod(Buzz.class, RuntimeMethodAnnotation.class));
    }
    
    
  
    
    @Test
    public void implementsInterfaceTrueTest() {
        assertEquals(true, ReflectionUtils.implementsInterface(new Foo(),EventListener.class));
    }
    
    @Test
    public void implementsInterfaceFalseTest() {
        assertEquals(false, ReflectionUtils.implementsInterface(new Bar(), EventListener.class));
    }
    
    @Test
    public void getMethodCountOneTest() {
        assertEquals(1, ReflectionUtils.getMethodCount(Foo.class));
    }
    
    @Test
    public void getMethodCountZeroTest() {
        assertEquals(0, ReflectionUtils.getMethodCount(Buzz.class));
    }
    
    @Test
    public void getMethodTest() {
        assertEquals("run", ReflectionUtils.getMethod(Bar.class,"run").getName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getMethodNoSuchMethodTest() {
        ReflectionUtils.getMethod(Buzz.class,"run");
    }
    
    @Test
    public void getMethodsTestOne() {
        Set<Method> methods = ReflectionUtils.getMethods(Bar.class,Void.TYPE,new Class<?>[]{});
        assertEquals(1, methods.size());
    }
    
    @Test
    public void getMethodsTestTwo() {
        Set<Method> methods = ReflectionUtils.getMethods(Bar.class);
        assertEquals(2, methods.size());
    }
    
    @Test
    public void getAnnotatedMethodTest(){
        Method method = ReflectionUtils.getAnnotatedMethod(Foo.class,RuntimeMethodAnnotation.class);
        assertEquals("run", method.getName());
    }
    
    @Test
    public void getAnnotatedMethodNotRuntimeAnnotationTest(){
        Method method = ReflectionUtils.getAnnotatedMethod(Bar.class,CompileMethodAnnotation.class);
        assertNull(method);
    }
    
    @Test
    public void getAnnotatedMethodNoMethodsTest(){
        Method method = ReflectionUtils.getAnnotatedMethod(Buzz.class,RuntimeMethodAnnotation.class);
        assertNull(method);
    }
    
    @Test
    public void getAnnotatedMethodsTest() {
        Set<Method> methods = ReflectionUtils.getAnnotatedMethods(Fuzz.class,RuntimeMethodAnnotation.class);
        assertEquals(2, methods.size());
        Method runMethod = ReflectionUtils.getMethod(Fuzz.class, "run");
        Method getNameMethod = ReflectionUtils.getMethod(Fuzz.class, "getName");
        assertTrue(methods.contains(runMethod));
        assertTrue(methods.contains(getNameMethod));
    }
    
    @Test
    public void getAnnotatedMethodsWrongAnnotationTest() {
        Set<Method> methods = ReflectionUtils.getAnnotatedMethods(Fuzz.class,RuntimeTypeAnnotation.class);
        assertEquals(0, methods.size());
      
    }
    
    @Test
    public void getInheritlyAnnotatedMethodsTest() {
        Set<Method> methods = ReflectionUtils.getInheritlyAnnotatedMethods(Fuzz.class,RuntimeParentAnnotation.class);
        assertEquals(2, methods.size());
        Method runMethod = ReflectionUtils.getMethod(Fuzz.class, "run");
        Method getNameMethod = ReflectionUtils.getMethod(Fuzz.class, "getName");
        assertTrue(methods.contains(runMethod));
        assertTrue(methods.contains(getNameMethod));
    }
    
    @Test
    public void getInheritlyAnnotatedMethodsOneMethodTest() {
        Set<Method> methods = ReflectionUtils.getInheritlyAnnotatedMethods(Foo.class,RuntimeParentAnnotation.class);
        assertEquals(1, methods.size());
        Method runMethod = ReflectionUtils.getMethod(Foo.class, "run");
        assertTrue(methods.contains(runMethod));
    }
    
    @Test
    public void getInheritlyAnnotatedMethodsNoInheritTest() {
        Set<Method> methods = ReflectionUtils.getInheritlyAnnotatedMethods(Tor.class,RuntimeParentAnnotation.class);
        assertEquals(0, methods.size());
        
    }
    
    @Test
    public void hasMethodVoidReturnTypeTrueTest() {
       assertTrue( ReflectionUtils.hasMethodVoidReturnType( ReflectionUtils.getMethod(Fuzz.class, "run")) );
    }
    
    @Test
    public void hasMethodVoidReturnTypeFalseTest() {
       assertFalse( ReflectionUtils.hasMethodVoidReturnType( ReflectionUtils.getMethod(Fuzz.class, "getName")) );
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
      
    
    
    @Ignore
    @RuntimeTypeAnnotation
    private static class Foo implements EventListener{
        @SuppressWarnings("unused")
        @RuntimeMethodAnnotation
        public void run(){}
    }
    
    @Ignore
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
    @Ignore
    private static class Bar {
        @SuppressWarnings("unused")
        @CompileMethodAnnotation
        public void run(){}
        
        @SuppressWarnings("unused")
        public String getName(){
            return "Bar";
        }
    }
    
    @Ignore
    public static class Buzz{
        
    }
    
    @Ignore
    public static class Fuzz{

        @RuntimeMethodAnnotation
        public void run(){}

        @RuntimeMethodAnnotation
        public String getName(){
            return "Bar";
        }
    }
}
