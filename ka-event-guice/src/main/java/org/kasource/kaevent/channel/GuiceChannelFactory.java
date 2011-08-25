package org.kasource.kaevent.channel;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;

import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.google.inject.Singleton;

/**
 * Guice Channel Factory.
 * 
 * Will allow channel instances to be created via Guice.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceChannelFactory extends ChannelFactoryImpl {

	@Inject
	private Injector injector;
	
	/**
	 * Constructor.
	 * 
	 * @param channelRegister     Channel Register.
	 * @param eventRegister       Event Register
	 * @param eventMethodInvoker  Event Method Invoker.
	 * @param beanResolver        Bean Resolver.
	 **/
	@Inject
	public GuiceChannelFactory(ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
		super(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
	}
	
	/**
	 * Returns a new Channel instance.
	 * 
	 * @param channelClass Class to instance of.
	 * @param name         Name of the channel.
	 * 
	 * @return new Channel instance.
	 * 
	 * @throws IllegalStateException if a channel with name already 
	 * created or channelClass could not be instancieated.
	 **/
     protected Channel getNewChannel(Class<? extends Channel> channelClass,
				String name) throws IllegalStateException {
    	 if (channelClass.equals(ChannelImpl.class)) {
    		 return super.getNewChannel(channelClass, name);
    	 }
    	 try {
    		 	 
    		 Channel channel = injector.getInstance(channelClass);
    		 channel.setName(name);
    		 return channel;
    	 } catch (ConfigurationException ce) {
    	     return super.getNewChannel(channelClass, name);
    	 } catch (ProvisionException pe) {
    	     return super.getNewChannel(channelClass, name);
    	 } catch (CreationException ce) {
    	     return super.getNewChannel(channelClass, name);
    	 }
    	 
	 }
	
}
