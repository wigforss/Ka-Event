package org.kasource.kaevent.spring.xml;

import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.config.SpringKaEventConfigurer;
import org.kasource.kaevent.event.config.EventFactoryBean;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class KaEventConfigurerBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	protected Class<?> getBeanClass(Element element) {
		return SpringKaEventConfigurer.class;
	}

	protected void doParse(Element element, ParserContext pc,
			BeanDefinitionBuilder bean) {
		element.setAttribute(ID_ATTRIBUTE, "kaEventConfigurer");
		bean.addPropertyValue("scanClassPath", element
				.getAttribute("scanClassPath"));

		bean.addConstructorArgReference(KaEventSpringBean.CONFIGURATION.getId());
		bean.setInitMethodName("configure");
		bean.setLazyInit(false);
		Set<KaEventSpringBean> excludeSet = new HashSet<KaEventSpringBean>();
		excludeSet.add(KaEventSpringBean.QUEUE_BEAN);
		createBeans(pc, excludeSet);
		String queueBean = element.getAttribute("queueBean");
		if (!queueBean.toLowerCase().equals("true")) {
			String queueClass = element.getAttribute("queueClass");
			String concurrent = element.getAttribute("concurrentQueue");
			if (queueClass != null
					&& queueClass.length() > 0
					&& !queueClass.equals(ThreadPoolQueueExecutor.class.getName())) {
				try {
					createQueueBean(Class.forName(queueClass), pc, false, concurrent);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			} else {
				createQueueBean(ThreadPoolQueueExecutor.class, pc, true, concurrent);
			}
		} 
	}

	protected void createBeans(ParserContext pc, Set<KaEventSpringBean> excludeSet) {
		for(KaEventSpringBean bean : KaEventSpringBean.values()) {
			if(!excludeSet.contains(bean)) {
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(bean.getBeanClass());
				
				for(String beanRef : bean.getConstructorRefs()) {
					builder.addConstructorArgReference(beanRef);
				}
				
				for(int i = 0; i < bean.getRefProperties().length; ++i) {
					builder.addPropertyReference(bean.getRefProperties()[i], bean.getPropertyRefValues()[i]);
				}
				builder.setLazyInit(false);
				pc.registerBeanComponent(new BeanComponentDefinition(builder
						.getBeanDefinition(), bean.getId()));
			}
		}
	}
	
	protected void createQueueBean(Class<?> clazz, ParserContext pc, boolean setEventSender, String concurrent) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(clazz);
		if(setEventSender) {
			builder.addConstructorArgReference(KaEventSpringBean.EVENT_SENDER.getId());
		}
		builder.addPropertyValue("concurrent", concurrent);
		builder.setLazyInit(false);
		pc.registerBeanComponent(new BeanComponentDefinition(builder
				.getBeanDefinition(), KaEventSpringBean.QUEUE_BEAN.getId()));

	}

}
