package org.kasource.kaevent.spring.xml;

import java.util.Arrays;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

public abstract class AbstractDecorator {

	/**
	 * Adds the bean with the beanName as a dependency to the bean 
	 * 
	 * @param bean		The bean that is changed
	 * @param beanName	Name of the bean that bean should depend on
	 **/
	protected void addDependsOn(BeanDefinitionHolder bean, String beanName) {
		AbstractBeanDefinition definition = ((AbstractBeanDefinition) bean.getBeanDefinition());
		  String[] dependsOn = definition.getDependsOn();
		  
		  if(dependsOn == null || dependsOn.length == 0) {
			  definition.setDependsOn(new String[]{beanName});
		  } else {
			  String[] newDepends = Arrays.copyOf(dependsOn, dependsOn.length+1);
			  newDepends[newDepends.length-1] = beanName;
			  definition.setDependsOn(new String[]{beanName});
		  }
	}
}
