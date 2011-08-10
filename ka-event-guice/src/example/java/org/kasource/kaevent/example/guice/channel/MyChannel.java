package org.kasource.kaevent.example.guice.channel;

import java.util.EventObject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ListenerChannelAdapter;
import org.kasource.kaevent.event.register.EventRegister;

import com.google.inject.Inject;


public class MyChannel extends ListenerChannelAdapter {

	
	@Inject
	public MyChannel(ChannelRegister channelRegister, EventRegister eventRegister, BeanResolver beanResolver) {
		super(channelRegister, eventRegister, beanResolver);
	}


	@Override
	public void fireEvent(EventObject event, boolean blocked) {
		// TODO Auto-generated method stub
		System.out.println(event);
	}

}
