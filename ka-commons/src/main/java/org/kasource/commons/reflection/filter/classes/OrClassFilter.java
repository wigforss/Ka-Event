package org.kasource.commons.reflection.filter.classes;

public class OrClassFilter implements ClassFilter {

    private ClassFilter left;
    private ClassFilter right;
    
    public OrClassFilter(ClassFilter left, ClassFilter right) {
        this.left = left;
        this.right = right;
    }
    
    
    @Override
    public boolean passFilter(Class<?> clazz) {
      return left.passFilter(clazz) || right.passFilter(clazz);
    }

}
