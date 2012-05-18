/**
 * 
 */
package org.kasource.kaevent.spring.xml;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.SpringBeanResolver;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Parses the beanResolver XML element.
 * 
 * @author Rikard Wigforss
 */
public class BeanResolverBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
     * Returns the class for the element.
     * 
     * if no class is configured the Spring Bean Resolver will be used.
     * 
     * @param element
     *            The beanResolver XML element.
     *            
     * @return Returns the beanResolver class to use.
     * 
     * @throws IllegalStateException if class attribute is set
     * to a class that does not implement the BeanResolver interface or
     * a class that can't be found.
     **/
    protected Class<?> getBeanClass(Element element) throws IllegalStateException {
        String className = element.getAttribute("class");
        if (className != null && className.length() > 0) {
            try {
                Class<?> clazz = Class.forName(className);
                if (!BeanResolver.class.isAssignableFrom(clazz)) {
                    throw new IllegalStateException("Class " + className + " must implement "
                                + BeanResolver.class.getName());
                }
                return clazz;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not find class " + className, e);
            }
        } else {
            return SpringBeanResolver.class;
        }
    }

   /**
    * Sets the id of the element to beanResolver.
    * 
    *  @param element The beanResolver XML element.
    *  @param bean    The bean definition.
    **/
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        element.setAttribute(ID_ATTRIBUTE, "beanResolver");
    }

}
