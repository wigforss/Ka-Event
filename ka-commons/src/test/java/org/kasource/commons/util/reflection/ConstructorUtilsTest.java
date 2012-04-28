package org.kasource.commons.util.reflection;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ConstructorUtilsTest {

    @Test
    public void getInstanceTest() {
        List list = ConstructorUtils.getInstance("java.util.ArrayList", List.class);
    }
    
    @Test(expected = IllegalStateException.class)
    public void getInstanceTestUnknownClass() {
        List list = ConstructorUtils.getInstance("org.rikard.MyClass", List.class);
    }
    
    @Test(expected = ClassCastException.class)
    public void getInstanceTestWrongTypeClass() {
        List list = ConstructorUtils.getInstance("java.lang.Integer", List.class, new Class<?>[]{int.class}, new Object[]{5});
    }
    
    @Test
    public void getInstanceOneParamTest() {
        Number number = ConstructorUtils.getInstance("java.lang.Integer", Number.class, new Class<?>[]{int.class}, new Object[]{5});
    }
    
    @Test(expected = IllegalStateException.class)
    public void getInstanceOneParamWrongTypeTest() {
        Number number = ConstructorUtils.getInstance("java.lang.Integer", Number.class, new Class<?>[]{List.class}, new Object[]{new ArrayList<Integer>()});
    }
}
