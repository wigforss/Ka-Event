package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MethodFilterBuilderTest {

    @TestedObject
    private MethodFilterBuilder builder;

    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }

    
    @Test
    public void notTest() {
        MethodFilter filter = builder.not().isPublic().build();
        assertTrue(filter instanceof NegationMethodFilter);
    }
    
    @Test
    public void orTest() {
        MethodFilter filter = builder.isPublic().or().isPrivate().or().isProtected().build();
        assertTrue(filter instanceof OrMethodFilter);
    }
    
    @Test
    public void listTest() {
        MethodFilter filter = builder.isPublic().isSynchronized().build();
        assertTrue(filter instanceof MethodFilterList);
    }
    
    @Test
    public void isPublic() {
        MethodFilter filter = builder.isPublic().build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

    @Test
    public void isProtected() {
        MethodFilter filter = builder.isProtected().build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

    @Test
    public void isPrivate() {
        MethodFilter filter = builder.isPrivate().build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

    @Test
    public void isStatic() {
        MethodFilter filter = builder.isStatic().build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

    @Test
    public void isSynchronized() {
        MethodFilter filter = builder.isSynchronized().build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

   
    @Test
    public void isDefault() {
        MethodFilter filter = builder.isDefault().build();
        assertTrue(filter instanceof NegationMethodFilter);
    }

    @Test
    public void byModifiers() {
        MethodFilter filter = builder.byModifiers(Modifier.PUBLIC & Modifier.STATIC).build();
        assertTrue(filter instanceof ModifierMethodFilter);
    }

    @Test
    public void name() {
        MethodFilter filter = builder.name("get[A-Z].*").build();
        assertTrue(filter instanceof NameMethodFilter);
    }

    @Test
    public void annotated() {
        MethodFilter filter = builder.annotated(Retention.class).build();
        assertTrue(filter instanceof AnnotatedMethodFilter);
    }

    @Test
    public void metaAnnotated() {
        MethodFilter filter = builder.metaAnnotated(Retention.class).build();
        assertTrue(filter instanceof MetaAnnotatedMethodFilter);
    }
    
    @Test
    public void hasSignature() {
        MethodFilter filter = builder.hasSignature(int.class).build();
        assertTrue(filter instanceof SignatureMethodFilter);
    }

    @Test
    public void hasReturnType() {
        MethodFilter filter = builder.returnType(Void.TYPE).build();
        assertTrue(filter instanceof ReturnTypeMethodFilter);
    }
    
    @Test
    public void returnTypeExtends() {
        MethodFilter filter = builder.returnTypeExtends(Void.TYPE).build();
        assertTrue(filter instanceof ReturnTypeAssignableFromMethodFilter);
    }

    @Test
    public void numberOfParameters() {
        MethodFilter filter = builder.numberOfParameters(4).build();
        assertTrue(filter instanceof NumberOfParametersMethodFilter);
    }

    @Test
    public void parametersAssignableFrom() {
        MethodFilter filter = builder.parametersExtendsType(List.class).build();
        assertTrue(filter instanceof AssignableFromMethodFilter);
    }

    @Test
    public void parameterAssignableFrom() {
        MethodFilter filter = builder.parameterExtendsType(0, List.class).build();
        assertTrue(filter instanceof AssignableFromMethodFilter);
    }

   

    @Test
    public void parameterTypeFilter() {
        MethodFilter filter = builder.parameterTypeFilter(0, new NameClassFilter("Abstract.*")).build();
        assertTrue(filter instanceof ParameterClassMethodFilter);
    }

    @Test
    public void parameterTypesFilter() {
        MethodFilter filter = builder.parametersTypesFilter(new NameClassFilter("Abstract.*")).build();
        assertTrue(filter instanceof ParameterClassMethodFilter);
    }

}
