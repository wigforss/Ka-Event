package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import com.kenai.sadelf.annotations.impl.BeanListener;
import com.kenai.sadelf.annotations.impl.ChannelListener;
import com.kenai.sadelf.channel.Channel;
import com.kenai.sadelf.event.dispatcher.EventDispatcher;
import com.kenai.sadelf.exception.CouldNotResolveBeanException;
import com.kenai.sadelf.exception.NoSuchChannelException;

/**
 * Register Listeners based on annotations
 * 
 * @author rikard
 * @version $Id$
 **/
public class RegisterListenerByAnnotationImpl implements RegisterListenerByAnnotation{

	
	private ChannelRegister channelRegister;
	private SourceObjectListenerRegister sourceObjectListenerRegister;
	private Map<EventListener, String[]> channelListeners = new HashMap<EventListener, String[]>();
	private Map<EventListener, String[]> beanListeners = new HashMap<EventListener, String[]>();

	
	/**
	 * Register <i>listener</i> using the attributes from <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void registerChannelListener(
			ChannelListener channelListenerAnnotation, Object listener) {
		if (eventDispatcher == null) {
			channelListeners.put((EventListener) listener,
					channelListenerAnnotation.value());
		} else {
			registerChannelListener((EventListener) listener,
					channelListenerAnnotation.value());
		}

	}

	
	private void registerChannelListener(EventListener listener,
			String[] channels) {
		for (String channelName : channels) {
			try {
				Channel channel = eventDispatcher.getChannelFactory().getChannel(
						channelName);
				channel.registerListener((EventListener) listener);
			}catch(NoSuchChannelException nse) {
				throw new NoSuchChannelException("Channel: " + channelName + " in @ChannelListener on " + listener.getClass()+" could not be found!");
			}
		}
	}

	/**
	 * Unregister <i>listener</i> using the attributes from <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void unregisterChannelListener(
			ChannelListener channelListenerAnnotation, Object listener) {
		 if(channelListenerAnnotation != null) {
	            for (String channelName : channelListenerAnnotation.value()) {
	                Channel channel = eventDispatcher.getChannelFactory().getChannel(channelName);
	                if(channel != null) {
	                	channel.unregisterListener((EventListener) listener);
	                }
	            }     
		 }	
	}
	
	/**
	 * Register <i>listener</i> using the attributes from <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void registerBeanListener(BeanListener beanListenerAnnotation,
			Object listener) {

		if (eventDispatcher == null) {
			beanListeners.put((EventListener) listener, beanListenerAnnotation
					.value());
		} else {
			registerBeanListener((EventListener) listener,
					beanListenerAnnotation.value());
		}

	}

	private void registerBeanListener(EventListener listener, String[] beanNames) {
		for (String beanName : beanNames) {
			try {
				Object eventSource = eventDispatcher.getEventConfigFactory().getBeanResolver().getBean(beanName);
				eventDispatcher.registerListener((EventListener) listener,
						eventSource);
			} catch(CouldNotResolveBeanException nrsb) {
				throw new CouldNotResolveBeanException("Bean: "+beanName+" in @BeanListener on "+listener.getClass()+" could not be found!",nrsb.getCause());
			}
			
		}
	}

	/**
	 * unregister <i>listener</i> using the attributes from <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void unregisterBeanListener(BeanListener beanListenerAnnotation,
			Object listener) {
		for (String beanName : beanListenerAnnotation.value()) {
            Object eventSource = eventDispatcher.getEventConfigFactory().getBeanResolver().getBean(beanName);
            eventDispatcher.unregisterListener((EventListener) listener, eventSource);
        }
		
	}
	
	/**
	 * If calls are made to the register methods before the {@link EventDispatcher} is set the information is
	 * stored, so that this method could register those {@link java.util.EventListener} implementations
	 * once the setEventDispatcher() method is called.
	 **/
	@Override
	public void initialize() {
		for (Map.Entry<EventListener, String[]> entry : channelListeners
				.entrySet()) {
			registerChannelListener(entry.getKey(), entry.getValue());
		}
		channelListeners.clear();
		for (Map.Entry<EventListener, String[]> entry : beanListeners
				.entrySet()) {
			registerBeanListener(entry.getKey(), entry.getValue());
		}
		beanListeners.clear();
	}

	
	/**
	 * Set the event dispatcher to use when registering {@link java.util.EventListener} implementations.
	 * 
	 * @param eventDispatcher the event dispatcher to be used
	 **/
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	
}
