package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

public interface MethodFilter {

    public boolean passFilter(Method method);
}
