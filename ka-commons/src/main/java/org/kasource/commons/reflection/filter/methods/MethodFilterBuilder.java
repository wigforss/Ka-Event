package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.ClassFilter;


public class MethodFilterBuilder {
    private enum Operator {NONE, NOT, OR};
    private List<MethodFilter> filters = new ArrayList<MethodFilter>();
    private Operator operator = Operator.NONE;
    
    private void add(MethodFilter filter) {
        switch(operator) {
        case NOT:
            filters.add(new NegationMethodFilter(filter));
            operator = Operator.NONE;
            break;
        case OR:
            filters.set(filters.size() - 1, 
                        new OrMethodFilter(filters.get(filters.size() - 1), filter));
            operator = Operator.NONE;
            break;
        default:
            filters.add(filter);
            break;
        }
    }
    
    public MethodFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    public MethodFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    
    public MethodFilterBuilder isPublic() {
        add(new ModifierMethodFilter(Modifier.PUBLIC));
        return this;
    }
    
    public MethodFilterBuilder isProtected() {
        add(new ModifierMethodFilter(Modifier.PROTECTED));
        return this;
    }
    
    public MethodFilterBuilder isPrivate() {
        add(new ModifierMethodFilter(Modifier.PRIVATE));
        return this;
    }
    
    public MethodFilterBuilder isStatic() {
        add(new ModifierMethodFilter(Modifier.STATIC));
        return this;
    }
    
    public MethodFilterBuilder isSynchronized() {
        add(new ModifierMethodFilter(Modifier.SYNCHRONIZED));
        return this;
    }
    
    public MethodFilterBuilder isDefault() {
        add(new NegationMethodFilter(new ModifierMethodFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    public MethodFilterBuilder byModifiers(int modifiers) {
        add(new ModifierMethodFilter(modifiers));
        return this;
    }
    
    public MethodFilterBuilder name(String nameRegExp) {
        add(new NameMethodFilter(nameRegExp));
        return this;
    }
    
    public MethodFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedMethodFilter(annotation));
        return this;
    }
    
    public MethodFilterBuilder hasSignature(Class<?>... params) {
        add(new SignatureMethodFilter(params));
        return this;
    }
    
    public MethodFilterBuilder hasReturnType(Class<?> returnType) {
        add(new ReturnTypeMethodFilter(returnType));
        return this;
    }
    
    public MethodFilterBuilder numberOfParameters(int numberOfParameters) {
        add(new NumberOfParametersMethodFilter(numberOfParameters));
        return this;
    }
    
    public MethodFilterBuilder parametersExtendsType(Class<?>... assignbleFromClass) {
        add(new AssignableFromMethodFilter(assignbleFromClass));
        return this;
    }
    
    public MethodFilterBuilder parameterExtendsType(int parameterIndex, Class<?> assignbleFromClass) {
        add(new AssignableFromMethodFilter(parameterIndex, assignbleFromClass));
        return this;
    }
    
    public MethodFilterBuilder inheritedAnnotation(Class<? extends Annotation> inheritedAnnotation) {
        add(new InheritlyAnnotatedMethodFilter(inheritedAnnotation));
        return this;
    }
    
    public MethodFilterBuilder parameterTypeFilter(int parameterIndex, ClassFilter filter) {
        add(new ParameterClassMethodFilter(parameterIndex, filter));
        return this;
    }
    
    public MethodFilterBuilder parametersTypesFilter(ClassFilter... filter) {    
        add(new ParameterClassMethodFilter(filter));
        return this;
    }
    
    public MethodFilter build() {
        if(filters.isEmpty()) {
            throw new IllegalStateException("No filter specified.");
        }
        if(filters.size() == 1) {
            return filters.get(0);
        }
        MethodFilter[] methodFilters = new MethodFilter[filters.size()];
        filters.toArray(methodFilters);
        return new MethodFilterList(methodFilters);
    }
    
}
