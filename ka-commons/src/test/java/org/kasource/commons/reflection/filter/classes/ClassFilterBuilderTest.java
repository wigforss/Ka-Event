package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.AnnotationClassFilter;
import org.kasource.commons.reflection.filter.classes.AssignableFromClassFilter;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.classes.ClassFilterBuilder;
import org.kasource.commons.reflection.filter.classes.ClassFilterList;
import org.kasource.commons.reflection.filter.classes.IsAnnotationClassFilter;
import org.kasource.commons.reflection.filter.classes.IsAnonymousClassFilter;
import org.kasource.commons.reflection.filter.classes.IsArrayClassFilter;
import org.kasource.commons.reflection.filter.classes.IsEnumClassFilter;
import org.kasource.commons.reflection.filter.classes.IsInterfaceClassFilter;
import org.kasource.commons.reflection.filter.classes.IsLocalClassFilter;
import org.kasource.commons.reflection.filter.classes.IsMemberClassFilter;
import org.kasource.commons.reflection.filter.classes.IsPrimitiveClassFilter;
import org.kasource.commons.reflection.filter.classes.IsSyntheticClassFilter;
import org.kasource.commons.reflection.filter.classes.ModifierClassFilter;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
import org.kasource.commons.reflection.filter.classes.NegationClassFilter;
import org.kasource.commons.reflection.filter.classes.OrClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

import java.util.List;


@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ClassFilterBuilderTest {

    @TestedObject
    private ClassFilterBuilder builder;
    
    @Test(expected = IllegalStateException.class)
    public void buildNoFilter() {
        builder.build();
    }
    
    @Test
    public void nameTest() {
        ClassFilter filter = builder.name("testName").build();
        assertTrue(filter instanceof NameClassFilter);
    }
    
    @Test
    public void isPublicTest() {
        ClassFilter filter = builder.isPublic().build();
        assertTrue(filter instanceof ModifierClassFilter);
    }
    
    @Test
    public void isStaticTest() {
        ClassFilter filter = builder.isStatic().build();
        assertTrue(filter instanceof ModifierClassFilter);
    }
    
    @Test
    public void isFinalTest() {
        ClassFilter filter = builder.isFinal().build();
        assertTrue(filter instanceof ModifierClassFilter);
    }
    
    @Test
    public void isAbstractTest() {
        ClassFilter filter = builder.isAbstract().build();
        assertTrue(filter instanceof ModifierClassFilter);
    }
    
    @Test
    public void notFilterTest() {
        ClassFilter filter = builder.not().isPublic().build();
        assertTrue(filter instanceof NegationClassFilter);
    }
    
    @Test
    public void orFilterTest() {
        ClassFilter filter = builder.isPrivate().or().isProtected().build();
        assertTrue(filter instanceof OrClassFilter);
    }
    
    @Test
    public void  isInterface() {
        ClassFilter filter = builder.isInterface().build();
        assertTrue(filter instanceof IsInterfaceClassFilter);
    }
    
    @Test
    public void isAnnotation() {
        ClassFilter filter = builder.isAnnotation().build();
        assertTrue(filter instanceof IsAnnotationClassFilter);
    }
    
    @Test
    public void isAnonymous() {
        ClassFilter filter = builder.isAnonymous().build();
        assertTrue(filter instanceof IsAnonymousClassFilter);
    }
    
    @Test
    public void isArray() {
        ClassFilter filter = builder.isArray().build();
        assertTrue(filter instanceof IsArrayClassFilter);
    }
    
    @Test
    public void isEnum() {
        ClassFilter filter = builder.isEnum().build();
        assertTrue(filter instanceof IsEnumClassFilter);
    }
    
    
    @Test
    public void isLocal() {
        ClassFilter filter = builder.isLocal().build();
        assertTrue(filter instanceof IsLocalClassFilter);      
    }
    
    @Test
    public void isMember() {
        ClassFilter filter = builder.isMember().build();
        assertTrue(filter instanceof IsMemberClassFilter);
    }
    
    @Test
    public void isPrimitive() {
        ClassFilter filter = builder.isPrimitive().build();
        assertTrue(filter instanceof IsPrimitiveClassFilter);
    }
    
    @Test
    public void isSynthetic() {
        ClassFilter filter = builder.isSynthetic().build();
        assertTrue(filter instanceof IsSyntheticClassFilter);
    }
    
    @Test
    public void extendsType() {
        ClassFilter filter = builder.extendsType(List.class).build();
        assertTrue(filter instanceof AssignableFromClassFilter);
    }
    
    @Test
    public void annotated() {
        ClassFilter filter = builder.annotated(Retention.class).build();
        assertTrue(filter instanceof AnnotationClassFilter);     
    }
    
    @Test
    public void metaAnnotated() {
        ClassFilter filter = builder.metaAnnotated(Retention.class).build();
        assertTrue(filter instanceof MetaAnnotatedClassFilter);     
    }
    
    @Test
    public void filterList() {
        ClassFilter filter = builder.isPrivate().isStatic().build();
        assertTrue(filter instanceof ClassFilterList);
       
    }
    
    @Test
    public void isDefault() {
        ClassFilter filter = builder.isDefault().build();
        assertTrue(filter instanceof NegationClassFilter);
    }
    
    @Test
    public void byModifiers() {
        ClassFilter filter = builder.byModifiers(Modifier.ABSTRACT).build();
        assertTrue(filter instanceof ModifierClassFilter);
    }
}
