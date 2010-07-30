package org.kasource.kaevent.listener.register;


import com.kenai.sadelf.annotations.impl.BeanListener;
import com.kenai.sadelf.annotations.impl.ChannelListener;
import com.kenai.sadelf.event.dispatcher.EventDispatcher;

/**
 * Registers {@link java.util.EventListener} implementations based on annotations
 * 
 * @author rikard
 * @version $Id$
 **/
public interface RegisterListenerByAnnotation {
	
	/**
	 * Set the event dispatcher to use when registering {@link java.util.EventListener} implementations.
	 * 
	 * @param eventDispatcher the event dispatcher to be used
	 **/
	public void setEventDispatcher(EventDispatcher eventDispatcher);
	
	/**
	 * Register <i>listener</i> using the attributes from <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	public void registerChannelListener(ChannelListener channelListenerAnnotation, Object listener);
	
	/**
	 * Unregister <i>listener</i> using the attributes from <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	public void unregisterChannelListener(ChannelListener channelListenerAnnotation, Object listener);
	
	/**
	 * Register <i>listener</i> using the attributes from <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	public void registerBeanListener(BeanListener beanListenerAnnotation, Object listener);
	
	/**
	 * unregister <i>listener</i> using the attributes from <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation The annotation with configuration
	 * @param listener					The {@link java.util.EventListener} implementation
	 **/
	public void unregisterBeanListener(BeanListener beanListenerAnnotation, Object listener);
	
	/**
	 * If calls are made to the register methods before the {@link EventDispatcher} is set the information is
	 * stored, so that this method could register those {@link java.util.EventListener} implementations
	 * once the setEventDispatcher() method is called.
	 **/
	public void initialize();
}
