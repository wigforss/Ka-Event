package org.kasource.spring.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;

/**
 * BeanExpressionResolver implementation that allows configuration of static methods and variables.
 * 
 * Registers itself on bean creation.
 * 
 * <pre>
 * <bean class="org.kasource.spring.expression.ConfigurableBeanExpressionResolver">
 *    <property name="staticMethodsByAnnotation">
 *      <!-- Will find all static methods annotated with ExpressionMethod in org.my.Utils and register them -->
 *       <property name="staticMethodsByAnnotation">
 *           <list>
 *               <value>org.my.Utils</value>
 *           </list>
 *       </property>
 *       <!-- Will register getInstance from java.util.Calendar as calendar -->
 *       <property name="staticMethods">
 *           <map>
 *               <entry key="calendar" value="java.util.Calendar.getInstance"/>
 *           </map>
 *       </property>
 *      <property name="variables">
 *           <map>
 *               <entry value="calendarClass" value="java.util.Calendar"/>
 *           </map>
 *       </property>
 * </bean>
 * </pre>
 * 
 * @author rikardwi
 **/
public class ConfigurableBeanExpressionResolver extends StandardBeanExpressionResolver implements BeanFactoryAware {
    
    private List<Class<?>> staticMethodsByAnnotation;
    private Map<String, String> staticMethods; 
    private Map<String, Object> variables;
    
    protected void customizeEvaluationContext(StandardEvaluationContext evalContext) {
        registerVariables(evalContext);
        registerFunctions(evalContext);
        registerAnnotatedFunctions(evalContext);
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            ConfigurableBeanFactory cbf = (ConfigurableBeanFactory) beanFactory;
            cbf.setBeanExpressionResolver(this);
        }

    }


    private void registerFunctions(StandardEvaluationContext evalContext) {
        if(staticMethods != null) {
            for(Map.Entry<String, String> entry :staticMethods.entrySet()) {
                Method method = getMethod(entry.getValue());
                if(method != null) {
                    evalContext.registerFunction(entry.getKey(), method);
                }
            }
        }
    }
    
    private void registerAnnotatedFunctions(StandardEvaluationContext evalContext) {
        if(staticMethodsByAnnotation != null) {
            for(Class<?> clazz : staticMethodsByAnnotation) {
                List<Method> annotatedMethods = getAnnotetdMethods(clazz);
                for(Method method : annotatedMethods) {
                    evalContext.registerFunction(method.getAnnotation(ExpressionMethod.class).value(), method);
                }
            }
        }
    }
    
    
    private void registerVariables(StandardEvaluationContext evalContext) {
        if(variables != null) {
            evalContext.setVariables(variables);
        }
    }
   
    private Method getMethod(String fullyQualifiedMethodName) {
        int index = fullyQualifiedMethodName.lastIndexOf('.');
        if(index != -1) {
            String className = fullyQualifiedMethodName.substring(0,index);
            String methodName = fullyQualifiedMethodName.substring(index + 1);
            Class<?> clazz = ClassUtils.resolveClassName(className.trim(), ClassUtils.getDefaultClassLoader());
            Method[] methods = clazz.getDeclaredMethods();
            Method methodFound = null;
            int minNumParams = Integer.MAX_VALUE;
            for(Method method : methods) {
                if(method.getName().equals(methodName.trim()) && method.isAccessible() && Modifier.isStatic(method.getModifiers())){
                    if(method.getParameterTypes().length < minNumParams) {
                        methodFound =  method;
                    }
                }
            }
            return methodFound;
        }
        return null;
    }
    
    private List<Method> getAnnotetdMethods(Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<Method>();
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method : methods) {
                
                if(method.isAnnotationPresent(ExpressionMethod.class) && method.isAccessible() && Modifier.isStatic(method.getModifiers())){
                    annotatedMethods.add(method);
                }
            }
        
        return annotatedMethods;
    }

    /**
     * @param staticMethodsByAnnotation the staticMethodsByAnnotation to set
     */
    public void setStaticMethodsByAnnotation(List<Class<?>> staticMethodsByAnnotation) {
        this.staticMethodsByAnnotation = staticMethodsByAnnotation;
    }

    /**
     * @param staticMethods the staticMethods to set
     */
    public void setStaticMethods(Map<String, String> staticMethods) {
        this.staticMethods = staticMethods;
    }

    /**
     * @param variables the variables to set
     */
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    
    
}
