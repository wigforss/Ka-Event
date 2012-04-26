package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.ClassFilter;


public class FieldFilterBuilder {
    private enum Operator {NONE, NOT, OR};
    private List<FieldFilter> filters = new ArrayList<FieldFilter>();
    private Operator operator = Operator.NONE;
    
    private void add(FieldFilter filter) {
        switch(operator) {
        case NOT:
            filters.add(new NegationFieldFilter(filter));
            operator = Operator.NONE;
            break;
        case OR:
            filters.set(filters.size() - 1, 
                        new OrFieldFilter(filters.get(filters.size() - 1), filter));
            operator = Operator.NONE;
            break;
        default:
            filters.add(filter);
            break;
        }
    }
    
    public FieldFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    public FieldFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    public FieldFilterBuilder name(String nameRegExp) {
        add(new NameFieldFilter(nameRegExp));
        return this;
    }
    
    public FieldFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedFieldFilter(annotation));
        return this;
    }
    
    public FieldFilterBuilder extendsType(Class<?> assignbleFromClass) {
        add(new AssignableFromFieldFilter(assignbleFromClass));
        return this;
    }
    
    
    public FieldFilterBuilder typeFilter(ClassFilter filter) {
        add(new FieldClassFieldFilter(filter));
        return this;
    }
    
    public FieldFilterBuilder isEnumConstant() {
        add(new IsEnumConstantFieldFilter());
        return this;
    }
    
    public FieldFilterBuilder isPublic() {
        add(new ModifierFieldFilter(Modifier.PUBLIC));
        return this;
    }
    
    public FieldFilterBuilder isProtected() {
        add(new ModifierFieldFilter(Modifier.PROTECTED));
        return this;
    }
    
    public FieldFilterBuilder isPrivate() {
        add(new ModifierFieldFilter(Modifier.PRIVATE));
        return this;
    }
    
    public FieldFilterBuilder isStatic() {
        add(new ModifierFieldFilter(Modifier.STATIC));
        return this;
    }
    
    public FieldFilterBuilder isTransient() {
        add(new ModifierFieldFilter(Modifier.TRANSIENT));
        return this;
    }
    
    public FieldFilterBuilder isFinal() {
        add(new ModifierFieldFilter(Modifier.FINAL));
        return this;
    }
    
    public FieldFilterBuilder isVolatile() {
        add(new ModifierFieldFilter(Modifier.VOLATILE));
        return this;
    }
    
    public FieldFilterBuilder isDefault() {
        add(new NegationFieldFilter(new ModifierFieldFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    public FieldFilterBuilder byModifiers(int modifiers) {
        add(new ModifierFieldFilter(modifiers));
        return this;
    }
    
    
    public FieldFilter build() {
        if (filters.isEmpty()) {
            throw new IllegalStateException("No field filters configured!");
        }
        if (filters.size() == 1) {
           return filters.get(0); 
        }
        FieldFilter[] fieldFilters = new FieldFilter[filters.size()];
        filters.toArray(fieldFilters);
        return new FieldFilterList(fieldFilters);
    }
}
