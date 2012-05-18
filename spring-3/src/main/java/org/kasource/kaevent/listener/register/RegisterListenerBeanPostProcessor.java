package org.kasource.kaevent.listener.register;

import org.kasource.commons.util.reflection.AnnotationsUtils;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotationImpl;

/**
 * Bean Post Processor that registers @ChannelListener or @BeanListener annotated
 * objects as listeners.
 * 
 * @author rikardwi
 **/
public class RegisterListenerBeanPostProcessor implements BeanPostProcessor {

    private RegisterListenerByAnnotation register;
    
    @Override
    public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
        
        BeanListener beanListener = AnnotationsUtils.getAnnotation(object.getClass(), BeanListener.class);
        if (beanListener != null) {
            getRegister().registerBeanListener(beanListener, object);
        }
        ChannelListener channelListener = AnnotationsUtils.getAnnotation(object.getClass(), ChannelListener.class);
        if (channelListener != null) {
            getRegister().registerChannelListener(channelListener, object);
        }
        return object;
    }

    private RegisterListenerByAnnotation getRegister() {
        if(register == null) {
            register = RegisterListenerByAnnotationImpl.getInstance();
        }
        return register;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object object, String beanName) throws BeansException {       
        return object;
    }

}
