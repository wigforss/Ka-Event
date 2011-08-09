package org.kasource.kaevent.channel;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Allows prototype beans to be created as channel objects.
 * 
 * @author rikardwi
 **/
public class SpringChannelFactory extends ChannelFactoryImpl implements
		ApplicationContextAware {

	private ApplicationContext applicationContext;

	public SpringChannelFactory(ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
		super(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
	}

	/**
	 * Allow prototype scoped beans to be created as channels.
	 **/
	protected Channel getNewChannel(Class<? extends Channel> channelClass,
			String name) throws IllegalStateException {
		String[] channelBeans = applicationContext
				.getBeanNamesForType(channelClass);
		for (String beanName : channelBeans) {
			if (applicationContext.isPrototype(beanName)) {
				Channel channel = (Channel) applicationContext
						.getBean(beanName);
				if (channel != null) {
					return channel;
				}
				break;
			}
		}
		return super.getNewChannel(channelClass, name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}
}
