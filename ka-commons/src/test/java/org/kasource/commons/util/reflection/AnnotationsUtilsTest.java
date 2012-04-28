package org.kasource.commons.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.kasource.commons.util.reflection.AnnotationsUtils;

public class AnnotationsUtilsTest {
    @Test
    public void hasRuntimeAnnotationTest() {
        assertEquals(true, AnnotationsUtils.isAnnotationPresent(Foo.class, RuntimeTypeAnnotation.class));     
    }
    
    @Test
    public void hasClassAnnotationTest() {
        assertEquals(false, AnnotationsUtils.isAnnotationPresent(Bar.class, CompileTypeAnnotation.class));      
    }
    
    
    @Test
    public void hasSuperClassRuntimeAnnotationTest() {
        assertEquals(true, AnnotationsUtils.isAnnotationPresent(Buzz.class, RuntimeTypeAnnotation.class));      
    }
   
    
    @Test
    public void getRuntimeAnnotationTest() {
        assertEquals(RuntimeTypeAnnotation.class, AnnotationsUtils.getAnnotation(Foo.class, RuntimeTypeAnnotation.class).annotationType());
    }
    
    @Test
    public void getRuntimeAnnotationNotFoundTest() {
        assertEquals(null, AnnotationsUtils.getAnnotation(Bar.class, CompileTypeAnnotation.class));
    }
    
    @Test
    public void getSuperClassRuntimeAnnotationTest() {
        assertEquals(RuntimeTypeAnnotation.class, AnnotationsUtils.getAnnotation(Buzz.class, RuntimeTypeAnnotation.class).annotationType());
    }
    
    
    @Test
    public void hasAnnotatatedMethodTrueTest() {
        assertEquals(true, AnnotationsUtils.hasAnnotatatedMethod(Foo.class, RuntimeMethodAnnotation.class));
    }
    
    
    @Test
    public void hasAnnotatatedMethodNoMethodsTest() {
        assertEquals(false, AnnotationsUtils.hasAnnotatatedMethod(Buzz.class, RuntimeMethodAnnotation.class));
    }
    
    @Test
    public void getAnnotatatedMethodTest() throws SecurityException, NoSuchMethodException {
        assertEquals(Foo.class.getMethod("run"), AnnotationsUtils.getAnnotatatedMethod(Foo.class, RuntimeMethodAnnotation.class));
    }
    
    @Test
    public void getAnnotatatedMethodNoMethodFoumdTest() throws SecurityException, NoSuchMethodException {
        assertEquals(null, AnnotationsUtils.getAnnotatatedMethod(Buzz.class, RuntimeMethodAnnotation.class));
    }
    
    @Test 
    public void findAnnotatedMethods() throws SecurityException, NoSuchMethodException {
        Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
        annotations.add(RuntimeMethodAnnotation.class);
        Map<Class<? extends Annotation>, Method> methodMap = AnnotationsUtils.findAnnotatedMethods(Foo.class, annotations);
        assertEquals(Foo.class.getMethod("run"), methodMap.get(RuntimeMethodAnnotation.class));
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
     @interface CompileTypeAnnotation {

    }
    
    @Target(ElementType.METHOD)
     @interface CompileMethodAnnotation {

    }
    
    @Target(ElementType.ANNOTATION_TYPE)
     @interface CompileParentAnnotation {

    }
    
    ///CLOVER:OFF

    @CompileTypeAnnotation
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
    private static class Buzz extends Foo{
        
    }
    
    ///CLOVER:OFF
    private static class Fuzz{

        @RuntimeMethodAnnotation
        public void run(){}

        @RuntimeMethodAnnotation
        public String getName(){
            return "Bar";
        }
    }
    
}
