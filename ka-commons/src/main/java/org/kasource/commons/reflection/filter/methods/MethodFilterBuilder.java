package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.ClassFilter;

/**
 * Builder for MethodFilters.
 * <p>
 * This class offers functionality for building more complex
 * filter compositions in an expressive manner. The filters added will be
 * evaluated with AND operator. This builder allows both NOT and OR as
 * additional operators.
 * <p>
 * Examples:
 * {@code
 *  MethodFilter publicGetter = new MethodFilterBuilder().isPublic().name("get[A-Z]\\w*").not().hasReturnType(Void.TYPE).build();
 *  MethodFilter nonPublicfilter = new MethdodFilterBuilder().not().isPublic().build();
 *  MethodFilter privateOrProtected = new MethodFilterBuilder().isPrivate().or().isProtected().build();
 * }
 * 
 * @author rikardwi
 **/
public class MethodFilterBuilder {
    private enum Operator {NONE, NOT, OR};
    private List<MethodFilter> filters = new ArrayList<MethodFilter>();
    private Operator operator = Operator.NONE;
    
    /**
     * Adds filter to filter list and apply operator.
     * 
     * @param filter filter to add.
     **/
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
    
    /**
     * Set current operator to NOT, which will be applied
     * on the next filter added.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    /**
     * Set current operator to OR, which will be applied
     * on the next filter added, by applying OR to the last filter
     * added and the next.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    /**
     * Adds a filter for public methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isPublic() {
        add(new ModifierMethodFilter(Modifier.PUBLIC));
        return this;
    }
    
    /**
     * Adds a filter for protected methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isProtected() {
        add(new ModifierMethodFilter(Modifier.PROTECTED));
        return this;
    }
    
    /**
     * Adds a filter for private methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isPrivate() {
        add(new ModifierMethodFilter(Modifier.PRIVATE));
        return this;
    }
    
    /**
     * Adds a filter for static methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isStatic() {
        add(new ModifierMethodFilter(Modifier.STATIC));
        return this;
    }
    
    /**
     * Adds a filter for synchronized methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isSynchronized() {
        add(new ModifierMethodFilter(Modifier.SYNCHRONIZED));
        return this;
    }
    
    /**
     * Adds a filter for default access methods only.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder isDefault() {
        add(new NegationMethodFilter(new ModifierMethodFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    /**
     * Adds a filter for methods methods that has the supplied 
     * modifiers.
     * 
     * @param modifiers Modifiers to match.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder byModifiers(int modifiers) {
        add(new ModifierMethodFilter(modifiers));
        return this;
    }
    
    /**
     * Adds a filter for methods which names matches
     * the supplied name regular expression.
     * 
     * @param nameRegExp Regular Expression to match method name with.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder name(String nameRegExp) {
        add(new NameMethodFilter(nameRegExp));
        return this;
    }
    
    /**
     * Adds a filter for methods which is annotated
     * with the supplied annotation.
     * 
     * @param annotation The annotation to match.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedMethodFilter(annotation));
        return this;
    }
    
    /**
     * Adds a filter for methods which is annotated
     * with an annotation which is annotated with 
     * the supplied annotation.
     * 
     * @param annotation The meta annotation to match method annotations with.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder metaAnnotated(Class<? extends Annotation> inheritedAnnotation) {
        add(new MetaAnnotatedMethodFilter(inheritedAnnotation));
        return this;
    }
    
    /**
     * Adds a filter for methods which has the exact signature (types) as
     * the supplied parameter types.
     * 
     * @param params    Varargs type of the parameters, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder hasSignature(Class<?>... params) {
        add(new SignatureMethodFilter(params));
        return this;
    }
    
    /**
     * Adds a filter for methods which has a
     * return type matching the supplied parameter returnType.
     * 
     * @param returnType The class to match return type with.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder returnType(Class<?> returnType) {
        add(new ReturnTypeMethodFilter(returnType));
        return this;
    }
    
    /**
     * Adds a filter for methods which has a
     * return type that extends the supplied parameter returnType.
     * 
     * @param returnType The class to match return type with.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder returnTypeExtends(Class<?> returnType) {
        add(new ReturnTypeAssignableFromMethodFilter(returnType));
        return this;
    }
    
    /**
     * Adds a filter for methods with matching number of
     * parameters as the supplied parameter: numberOfParameters.
     * 
     * @param numberOfParameters The number of parameters to match.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder numberOfParameters(int numberOfParameters) {
        add(new NumberOfParametersMethodFilter(numberOfParameters));
        return this;
    }
    
    /**
     * Adds a filter that allows only methods which parameter types extends the supplied
     * types in the extendsClass parameter. 
     * 
     * Only methods with same number of parameters as the extendsClass parameter will
     * be allowed.   
     * 
     * @param extendsClass Varargs classes that the parameters types should extend, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parametersExtendsType(Class<?>... extendsClass) {
        add(new AssignableFromMethodFilter(extendsClass));
        return this;
    }
    
    /**
     * Adds a filter for methods that has a parameter which extends the supplied type in the
     * extendsType parameter.
     * 
     * If a method does not have a parameter at parameterIndex (too few parameters) its not allowed. 
     * 
     * @param parameterIndex    The index of the parameter to inspect.
     * @param extendsClass      The class the parameter should extend.
     * @return
     **/
    public MethodFilterBuilder parameterExtendsType(int parameterIndex, Class<?> extendsClass) {
        add(new AssignableFromMethodFilter(parameterIndex, extendsClass));
        return this;
    }
    
   
    /**
     * Adds a filter for methods that has a parameter which type passes the supplied class filter.
     * 
     * If a method does not have a parameter at parameterIndex (too few parameters) its not allowed. 
     * <p>
     * Example:
     * {@code
     *  MethodFilter filter = new MethodFilterBuilder().parameterTypeFilter(0, new ClassFilterBuilder().annotated(MyAnnotation.class).build()).build();
     * }
     * Created a filter for methods which first parameter type should be annotated with @MyAnnotation.
     * 
     * @param parameterIndex    The index of the parameter to inspect.
     * @param filter            The filter which the parameter type should pass.
     * @return
     **/
    public MethodFilterBuilder parameterTypeFilter(int parameterIndex, ClassFilter filter) {
        add(new ParameterClassMethodFilter(parameterIndex, filter));
        return this;
    }
    
    /**
     * Adds a filter that allows only methods which parameter types passes the class filter supplied
     * in the parameter filter. 
     * 
     * Only methods with same number of parameters as the filter parameter will
     * be allowed.   
     * 
     * @param filter Varargs class filters that the parameters should pass, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public MethodFilterBuilder parametersTypesFilter(ClassFilter... filter) {    
        add(new ParameterClassMethodFilter(filter));
        return this;
    }
    
    /**
     * Returns the MethodFilter built.
     * 
     * @return the MethodFilter built.
     * @throws IllegalStateException if no filter was specified before calling build()
     **/
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
