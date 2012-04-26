package org.kasource.commons.reflection.filter.classes;

public class NameClassFilter implements ClassFilter {

    private String nameRegExp;
    
    public NameClassFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return clazz.getName().matches(nameRegExp);
    }

}
