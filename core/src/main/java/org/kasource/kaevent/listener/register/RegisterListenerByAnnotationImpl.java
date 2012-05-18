package org.kasource.kaevent.listener.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;

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
	private Map<Object, String[]> channelListeners = new ConcurrentHashMap<Object, String[]>();
	private Map<Object, String[]> beanListeners = new ConcurrentHashMap<Object, String[]>();

	
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
			channelListeners.put(listener,
					channelListenerAnnotation.value());
		} else {
			registerChannelListener(listener,
					channelListenerAnnotation.value());
		}

	}

	private void registerChannelListener(Object listener,
			String[] channels) {
		for (String channelName : channels) {
			try {
				Channel channel = channelRegister.getChannel(channelName);
				if(channel instanceof ListenerChannel) {
					((ListenerChannel) channel).registerListener(listener);
				} else {
					throw new IllegalArgumentException("Can't register " + listener + " to channel "+channelName + " its not an ListenerChannel.");
				}
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
				if(channel != null) {
					if(channel instanceof ListenerChannel) {
						((ListenerChannel) channel).unregisterListener(listener);
					} else {
						throw new IllegalArgumentException("Can't unregister " + listener + " to channel "+channelName + " its not an ListenerChannel.");
					}
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
	 *            The listener object.
	 **/
	@Override
	public void registerBeanListener(BeanListener beanListenerAnnotation,
			Object listener) {

		if (sourceObjectListenerRegister == null) {
			beanListeners.put((Object) listener, beanListenerAnnotation
					.value());
		} else {
			registerBeanListener(listener,
					beanListenerAnnotation.value());
		}

	}

	private void registerBeanListener(Object listener, String[] beanNames) {
		for (String beanName : beanNames) {
			try {
				Object eventSource = beanResolver.getBean(beanName, Object.class);
				sourceObjectListenerRegister.registerListener(
						(Object) listener, eventSource);
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
			Object eventSource = beanResolver.getBean(beanName, Object.class);
			sourceObjectListenerRegister.unregisterListener(
					listener, eventSource);
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
		for (Map.Entry<Object, String[]> entry : channelListeners
				.entrySet()) {
			registerChannelListener(entry.getKey(), entry.getValue());
		}
		channelListeners.clear();
		for (Map.Entry<Object, String[]> entry : beanListeners
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
