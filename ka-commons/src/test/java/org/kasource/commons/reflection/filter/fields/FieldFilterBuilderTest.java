package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class FieldFilterBuilderTest {
    
    @TestedObject
    private FieldFilterBuilder builder;
    
    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }
    
    @Test
    public void extendsType() {
        FieldFilter filter = builder.extendsType(List.class).build();
        assertTrue(filter instanceof AssignableFromFieldFilter);
    }
    
    @Test
    public void typeFilter() {
        FieldFilter filter = builder.typeFilter(new NameClassFilter("Abstract*.")).build();
        assertTrue(filter instanceof FieldClassFieldFilter);
    }
    
    @Test
    public void isEnumConstant() {
        FieldFilter filter = builder.isEnumConstant().build();
        assertTrue(filter instanceof IsEnumConstantFieldFilter);
    }
    
    @Test
    public void name() {
        FieldFilter filter = builder.name(".*List.*").build();
        assertTrue(filter instanceof NameFieldFilter);
    }
    
    @Test
    public void annotated() {
        FieldFilter filter = builder.annotated(Retention.class).build();
        assertTrue(filter instanceof AnnotatedFieldFilter);
    }
    
    @Test
    public void notTest() {
        FieldFilter filter = builder.not().isPublic().build();
        assertTrue(filter instanceof NegationFieldFilter);
    }
    
    @Test
    public void orTest() {
        FieldFilter filter = builder.isPrivate().or().isProtected().build();
        assertTrue(filter instanceof OrFieldFilter);
    }
    
    @Test
    public void isPublic() {
        FieldFilter filter = builder.isPublic().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isProtected() {
        FieldFilter filter = builder.isProtected().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isPrivate() {
        FieldFilter filter = builder.isPrivate().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isStatic() {
        FieldFilter filter = builder.isStatic().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isTransient() {
        FieldFilter filter = builder.isTransient().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isFinal() {
        FieldFilter filter = builder.isFinal().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isVolatile() {
        FieldFilter filter = builder.isVolatile().build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void isDefault() {
        FieldFilter filter = builder.isDefault().build();
        assertTrue(filter instanceof NegationFieldFilter);
    }
    
    @Test
    public void byModifiers() {
        FieldFilter filter = builder.byModifiers(Modifier.PROTECTED & Modifier.ABSTRACT).build();
        assertTrue(filter instanceof ModifierFieldFilter);
    }
    
    @Test
    public void listTest() {
        FieldFilter filter = builder.isStatic().isFinal().build();
        assertTrue(filter instanceof FieldFilterList);
    }
    
}
