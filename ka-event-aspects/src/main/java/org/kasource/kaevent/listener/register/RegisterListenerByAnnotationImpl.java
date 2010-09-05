package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventConfigurer;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.listener.implementations.BeanListener;
import org.kasource.kaevent.listener.implementations.ChannelListener;

/**
 * Register Listeners based on annotations
 * 
 * @author Rikard Wigforss
 * @version $Id: RegisterListenerByAnnotationImpl.java 2 2010-07-30 16:17:31Z
 *          wigforss $
 **/
public class RegisterListenerByAnnotationImpl implements
		RegisterListenerByAnnotation, KaEventInitializedListener {

    private static RegisterListenerByAnnotationImpl INSTANCE = new RegisterListenerByAnnotationImpl();
    
	private ChannelRegister channelRegister;
	private SourceObjectListenerRegister sourceObjectListenerRegister;
	private BeanResolver beanResolver;
	private Map<EventListener, String[]> channelListeners = new HashMap<EventListener, String[]>();
	private Map<EventListener, String[]> beanListeners = new HashMap<EventListener, String[]>();

	
	private RegisterListenerByAnnotationImpl(){
		KaEventInitializer.getInstance().addListener(this);
	}
	
	
	public static RegisterListenerByAnnotationImpl getInstance() {
	    return INSTANCE;
	}
	
	/**
	 * Register <i>listener</i> using the attributes from
	 * <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation
	 *            The annotation with configuration
	 * @param listener
	 *            The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void registerChannelListener(
			ChannelListener channelListenerAnnotation, Object listener) {
		if (channelRegister == null) {
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
				Channel channel = channelRegister.getChannel(channelName);
				channel.registerListener((EventListener) listener);
			} catch (NoSuchChannelException nse) {
				throw new NoSuchChannelException("Channel: " + channelName
						+ " in @ChannelListener on " + listener.getClass()
						+ " could not be found!");
			}
		}
	}

	/**
	 * Unregister <i>listener</i> using the attributes from
	 * <i>channelListenerAnnotation</i>.
	 * 
	 * @param channelListenerAnnotation
	 *            The annotation with configuration
	 * @param listener
	 *            The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void unregisterChannelListener(
			ChannelListener channelListenerAnnotation, Object listener) {
		if (channelListenerAnnotation != null) {
			for (String channelName : channelListenerAnnotation.value()) {
				Channel channel = channelRegister.getChannel(channelName);
				if (channel != null) {
					channel.unregisterListener((EventListener) listener);
				}
			}
		}
	}

	/**
	 * Register <i>listener</i> using the attributes from
	 * <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation
	 *            The annotation with configuration
	 * @param listener
	 *            The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void registerBeanListener(BeanListener beanListenerAnnotation,
			Object listener) {

		if (sourceObjectListenerRegister == null) {
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
				Object eventSource = beanResolver.getBean(beanName);
				sourceObjectListenerRegister.registerListener(
						(EventListener) listener, eventSource);
			} catch (CouldNotResolveBeanException nrsb) {
				throw new CouldNotResolveBeanException("Bean: " + beanName
						+ " in @BeanListener on " + listener.getClass()
						+ " could not be found!", nrsb.getCause());
			}

		}
	}

	/**
	 * unregister <i>listener</i> using the attributes from
	 * <i>beanListenerAnnotation</i>.
	 * 
	 * @param beanListenerAnnotation
	 *            The annotation with configuration
	 * @param listener
	 *            The {@link java.util.EventListener} implementation
	 **/
	@Override
	public void unregisterBeanListener(BeanListener beanListenerAnnotation,
			Object listener) {
		for (String beanName : beanListenerAnnotation.value()) {
			Object eventSource = beanResolver.getBean(beanName);
			sourceObjectListenerRegister.unregisterListener(
					(EventListener) listener, eventSource);
		}

	}

	/**
	 * If calls are made to the register methods before this method
	 * is called the information is stored, so that this
	 * method could register those {@link java.util.EventListener}
	 * implementations.
	 **/
	public void initialize(ChannelRegister channelRegister,
			SourceObjectListenerRegister sourceObjectListenerRegister,
			BeanResolver beanResolver) {
		this.channelRegister = channelRegister;
		this.sourceObjectListenerRegister = sourceObjectListenerRegister;
		this.beanResolver = beanResolver;
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


    
    @Override
    public void doInitialize(KaEventConfiguration configuration) {
       initialize(configuration.getChannelRegister(), configuration.getSourceObjectListenerRegister(), configuration.getBeanResolver());
        
    }

}
