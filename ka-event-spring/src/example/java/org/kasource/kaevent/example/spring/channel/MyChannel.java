package org.kasource.kaevent.example.spring.channel;

import java.util.EventObject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ListenerChannelAdapter;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MyChannel extends ListenerChannelAdapter {

	
	@Autowired
	public MyChannel(ChannelRegister channelRegister, EventRegister eventRegister, BeanResolver beanResolver) {
		super(channelRegister, eventRegister, beanResolver);
	}


	@Override
	public void fireEvent(EventObject event, boolean blocked) {
		// TODO Auto-generated method stub
		System.out.println(event);
	}

}
