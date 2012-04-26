package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public class OrMethodFilter implements MethodFilter {

    private MethodFilter left;
    private MethodFilter right;
    private MethodFilter[] rest;
    
    /**
     * Constructs an Or filter based on at least two filters left and right, but may include
     * additional number of filters as the rest.
     * 
     * @param left  Left side filter
     * @param right Right side filters
     * @param rest  Additional filters, may be empty.
     **/
    public OrMethodFilter(MethodFilter left, MethodFilter right, MethodFilter... rest) {
        this.left = left;
        this.right = right;
        this.rest = rest;
    }
    
    @Override
    public boolean passFilter(Method method) {
        boolean result = left.passFilter(method) || right.passFilter(method);
        for(MethodFilter filter : rest) {
            result |= filter.passFilter(method);
        }
        return result;
    }

}
