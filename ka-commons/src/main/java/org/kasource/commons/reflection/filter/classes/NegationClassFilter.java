package org.kasource.commons.reflection.filter.classes;

public class NegationClassFilter implements ClassFilter {

    private ClassFilter filter;
    
    public NegationClassFilter(ClassFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return !filter.passFilter(clazz);
    }

}
