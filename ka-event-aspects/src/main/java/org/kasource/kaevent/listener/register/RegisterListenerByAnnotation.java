package org.kasource.kaevent.listener.register;

import org.kasource.kaevent.listener.implementations.BeanListener;
import org.kasource.kaevent.listener.implementations.ChannelListener;



/**
 * Registers {@link java.util.EventListener} implementations based on annotations
 * 
 * @author Rikard Wigforss
 * @version $Id$
 **/
public interface RegisterListenerByAnnotation {
	
	
	
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
	
	
}
