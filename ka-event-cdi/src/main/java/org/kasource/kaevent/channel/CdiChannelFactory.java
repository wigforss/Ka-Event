package org.kasource.kaevent.channel;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;

@Dependent
public class CdiChannelFactory extends ChannelFactoryImpl implements Serializable{
 /*
	public CdiChannelFactory() {
		super();
		// TODO Auto-generated constructor stub
	}
*/
	@Inject
	public CdiChannelFactory(ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
		super(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
		
	}

}
