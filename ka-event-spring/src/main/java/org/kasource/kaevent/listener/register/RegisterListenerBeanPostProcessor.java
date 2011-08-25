package org.kasource.kaevent.listener.register;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Bean Post Processor that registers @ChannelListener or @BeanListener annotated
 * objects as listeners.
 * 
 * @author rikardwi
 **/
public class RegisterListenerBeanPostProcessor implements BeanPostProcessor {

    private RegisterListenerByAnnotation register = RegisterListenerByAnnotationImpl.getInstance();
    
    @Override
    public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
        BeanListener beanListener = ReflectionUtils.getAnnotation(object.getClass(), BeanListener.class);
        if (beanListener != null) {
            register.registerBeanListener(beanListener, object);
        }
        ChannelListener channelListener = ReflectionUtils.getAnnotation(object.getClass(), ChannelListener.class);
        if (channelListener != null) {
            register.registerChannelListener(channelListener, object);
        }
        return object;
    }

    @Override
    public Object postProcessBeforeInitialization(Object object, String beanName) throws BeansException {       
        return object;
    }

}
