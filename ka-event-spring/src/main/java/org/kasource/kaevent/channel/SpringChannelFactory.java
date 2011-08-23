package org.kasource.kaevent.channel;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
		if (channelClass.equals(ChannelImpl.class)) {
			return super.getNewChannel(channelClass, name);
		}
		String[] channelBeans = applicationContext
				.getBeanNamesForType(channelClass);
		for (String beanName : channelBeans) {
			if (applicationContext.isPrototype(beanName)) {
				Channel channel = (Channel) applicationContext
						.getBean(beanName);
				if (channel != null) {
					channel.setName(name);
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
