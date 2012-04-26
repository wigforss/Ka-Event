package org.kasource.commons.reflection.filter.classes;

public class ModifierClassFilter implements ClassFilter {

    private int modifier;
    
    public ModifierClassFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return (clazz.getModifiers() & modifier) > 0;
    }

}
