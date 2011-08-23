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
 * @author Rikard Wigforss
 */
public class BeanResolverBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
   
    protected Class<?> getBeanClass(Element element) {
        String className = element.getAttribute("class");
        if (className != null && className.length() > 0) {
        try {
            Class<?> clazz = Class.forName(className);
            if (!BeanResolver.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException("Class " + className 
                		+ " must implement " + BeanResolver.class.getName());
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find class " + className, e);
        }
        } else {
            return SpringBeanResolver.class;
        }
     }

     protected void doParse(Element element, BeanDefinitionBuilder bean) {
         element.setAttribute(ID_ATTRIBUTE, "beanResolver");
     }
     
   

}
