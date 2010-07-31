package org.kasource.kaevent.core.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.core.channel.Channel;
import org.kasource.kaevent.core.channel.ChannelRegister;
import org.kasource.kaevent.core.event.config.EventConfig;
import org.kasource.kaevent.core.event.register.EventRegister;
import org.kasource.kaevent.core.listener.register.SourceObjectListenerRegister;

public class EventMethodCaller {
	private ChannelRegister channelRegister;
	private SourceObjectListenerRegister sourceObjectListenerRegister;
	private EventRegister eventRegister;
	
	public void dispatcEvent(EventObject event, boolean blocked) {
		Set<Channel>  channels = channelRegister.getChannelsByEvent(event.getClass());
		for(Channel channel : channels) {
			channel.fireEvent(event, blocked);
		}
		Set<EventListener> listeners = sourceObjectListenerRegister.getListenersByEvent(event);
		for(EventListener listener : listeners) {
			try {
				dispatchEvent(event, listener);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void dispatchEvent(EventObject event, EventListener listener) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		EventConfig eventConfig = eventRegister.getEventByClass(event.getClass());
		Method eventMethod = eventConfig.getEventMethod(event);	
		eventMethod.invoke(listener, event);
	}
}
