package org.kasource.kaevent.example.spring.channel;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;

public class MyChannel extends ChannelImpl {

	public MyChannel(String name, ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
		super(name, channelRegister, eventRegister, eventMethodInvoker, beanResolver);
		System.out.println("My Channel created");
	}

}
