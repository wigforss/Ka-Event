package org.kasource.kaevent.channel;


import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;

@Dependent
public class CdiChannelFactory extends ChannelFactoryImpl {

    @Inject
    private BeanManager beanManager;
    
	@Inject
	public CdiChannelFactory(ChannelRegister channelRegister,
			EventRegister eventRegister, EventMethodInvoker eventMethodInvoker,
			BeanResolver beanResolver) {
	    
		super(channelRegister, eventRegister, eventMethodInvoker, beanResolver);
		
	}

	
	/**
     * Allow prototype scoped beans to be created as channels.
     * 
     * @param channelClass Channel implementation class to instanciate.
     * @param name           Name of the channel.
     * 
     * @return a new Channel instance.
     * 
     * @throws IllegalStateException if a channel with name already created or channelClass could not be instancieated.
     **/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Channel getNewChannel(Class<? extends Channel> channelClass,
            String name) throws IllegalStateException {
        if (channelClass.equals(ChannelImpl.class)) {
            return super.getNewChannel(channelClass, name);
        }
        try {
            Set<Bean<?>> beans = beanManager.getBeans(channelClass);
            for(Bean bean : beans) {   
               Channel channel = (Channel) beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
               channel.setName(name);
               return channel;
                
            }
        } catch(Exception e) {
        }
        return super.getNewChannel(channelClass, name);
    }
}
