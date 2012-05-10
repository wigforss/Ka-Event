package org.kasource.kaevent.spring.xml;

import org.kasource.kaevent.config.SpringKaEventConfigurer;
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
		element.setAttribute(ID_ATTRIBUTE, KaEventSpringBean.CONFIGURER_ID);
		bean.addPropertyValue("scanClassPath", element
				.getAttribute("scanClassPath"));

		bean.addConstructorArgReference(KaEventSpringBean.CONFIGURATION.getId());
		bean.setInitMethodName("configure");
		bean.setLazyInit(false);
		createBeans(pc);
	}

	
	
	/**
	 * Create beans needed by Ka-event, by looking at the KaEventSpringBean enumeration.
	 * 
	 * @param pc           Parser Context.
	 * @param excludeSet   Set of bean to exclude from creation.
	 **/
	private void createBeans(ParserContext pc) {
		for (KaEventSpringBean bean : KaEventSpringBean.values()) {
			 addBean(bean, pc);
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
	

}
