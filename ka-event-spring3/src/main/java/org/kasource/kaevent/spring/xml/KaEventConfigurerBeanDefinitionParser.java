package org.kasource.kaevent.spring.xml;

import java.util.HashSet;
import java.util.Set;

import org.kasource.kaevent.config.SpringKaEventConfigurer;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Parses the kaevent XML element.
 * 
 * @author rikardwi
 **/
public class KaEventConfigurerBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

    /**
     * Returns SpringKaEventConfigurer.
     * 
     * @param element kaevent XML element.
     * 
     * @return SpringKaEventConfigurer class.
     **/
	protected Class<?> getBeanClass(Element element) {
		return SpringKaEventConfigurer.class;
	}

	/**
	 * parse the kaevent XML element.
	 * 
	 * @param element kaevent XML element.
	 * @param pc      Parser context.
	 * @param bean    Bean definition.
	 **/
	@Override
	protected void doParse(Element element, ParserContext pc,
			BeanDefinitionBuilder bean) {
		element.setAttribute(ID_ATTRIBUTE, "kaEvent.configurer");
		bean.addPropertyValue("scanClassPath", element
				.getAttribute("scanClassPath"));

		bean.addConstructorArgReference(KaEventSpringBean.CONFIGURATION.getId());
		bean.setInitMethodName("configure");
		bean.setLazyInit(false);
		Set<KaEventSpringBean> excludeSet = new HashSet<KaEventSpringBean>();
		excludeSet.add(KaEventSpringBean.QUEUE_BEAN);
		createBeans(pc, excludeSet);
		configureQueue(element, pc);
	}

	/**
	 * Configure Event Queue.
	 * @param element kaevent XML element.
	 * @param pc      Parser Context.
	 **/
	private void configureQueue(Element element, ParserContext pc) {
	    String queueBean = element.getAttribute("queueBean");
        if (!"true".equals(queueBean.toLowerCase())) {
            String queueClass = element.getAttribute("queueClass");
            String concurrent = element.getAttribute("concurrentQueue");
            configureQueue(queueClass, pc, concurrent);
        } 
	}
	
	/**
	 * Configure Event Queue.
	 * 
	 * @param queueClass   Class name of queue class.
	 * @param pc           Parser Context.
	 * @param concurrent   true for Concurrent queue.
	 **/
	private void configureQueue(String queueClass, ParserContext pc, String concurrent) {
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
	
	/**
	 * Create beans needed by Ka-event, by looking at the KaEventSpringBean enumeration.
	 * 
	 * @param pc           Parser Context.
	 * @param excludeSet   Set of bean to exclude from creation.
	 **/
	private void createBeans(ParserContext pc, Set<KaEventSpringBean> excludeSet) {
		for (KaEventSpringBean bean : KaEventSpringBean.values()) {
			if (!excludeSet.contains(bean)) {
			    addBean(bean, pc);
			}
		}
	}
	
	/**
	 * Adds a bean to the parser context.
	 * 
	 * @param bean Bean to add.
	 * @param pc   Parse Context to register bean in.
	 **/
	private void addBean(KaEventSpringBean bean, ParserContext pc) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(bean.getBeanClass());
        if (bean.getInitMethod().length() > 0) {
            builder.setInitMethodName(bean.getInitMethod());
        }
        for (String beanRef : bean.getConstructorRefs()) {
            builder.addConstructorArgReference(beanRef);
        }
        
        for (int i = 0; i < bean.getRefProperties().length; ++i) {
            builder.
                addPropertyReference(bean.getRefProperties()[i], 
                                     bean.getPropertyRefValues()[i]);
        }
        builder.setLazyInit(false);
        pc.registerBeanComponent(new BeanComponentDefinition(builder
                .getBeanDefinition(), bean.getId()));
    
	}
	
	/**
	 * Creates and registers the event queue bean.
	 * 
	 * @param clazz            Queue Class.
	 * @param pc               Parser Context to register bean in.
	 * @param setEventRouter   true to set the Event Router.
	 * @param concurrent       true for concurrent
	 **/
	private void createQueueBean(Class<?> clazz, ParserContext pc, boolean setEventRouter, String concurrent) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(clazz);
		if (setEventRouter) {
		    builder.addPropertyReference("eventRouter", KaEventSpringBean.EVENT_ROUTER.getId());
		}
		builder.addPropertyValue("concurrent", concurrent);
		builder.setLazyInit(false);
		pc.registerBeanComponent(new BeanComponentDefinition(builder
				.getBeanDefinition(), KaEventSpringBean.QUEUE_BEAN.getId()));

	}

}
