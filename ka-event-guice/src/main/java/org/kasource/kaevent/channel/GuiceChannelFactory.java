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

@Singleton
public class GuiceChannelFactory extends ChannelFactoryImpl {

	@Inject
	private Injector injector;
	
	
	@Inject
	public GuiceChannelFactory(ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
		super(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
	}
	
	
     protected Channel getNewChannel(Class<? extends Channel> channelClass,
				String name) throws IllegalStateException {
    	 if(channelClass.equals(ChannelImpl.class)) {
    		 return super.getNewChannel(channelClass, name);
    	 }
    	 try {
    		 	 
    		 Channel channel = injector.getInstance(channelClass);
    		 channel.setName(name);
    		 return channel;
    	 } catch (ConfigurationException ce) {
    		 
    	 } catch( ProvisionException pe) {
    		 
    	 } catch( CreationException ce) {
    		 
    	 }
    	 return super.getNewChannel(channelClass, name);
	 }


	
}
