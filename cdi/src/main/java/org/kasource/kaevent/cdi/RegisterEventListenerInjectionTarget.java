package org.kasource.kaevent.cdi;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.kasource.commons.util.reflection.AnnotationsUtils;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotation;
import org.kasource.kaevent.listener.register.RegisterListenerByAnnotationImpl;


public  class RegisterEventListenerInjectionTarget<T> implements InjectionTarget<T> {
   private RegisterListenerByAnnotation register = RegisterListenerByAnnotationImpl.getInstance();
   
   private InjectionTarget<T> injectionTarget;

   
    public RegisterEventListenerInjectionTarget(InjectionTarget<T> injectionTarget) {
        this.injectionTarget = injectionTarget;
    }


    
    @Override
    public void dispose(T instance) {
        injectionTarget.dispose(instance);
        
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        
        return injectionTarget.getInjectionPoints();
    }

    @Override
    public T produce(CreationalContext<T> ctx) {
        return injectionTarget.produce(ctx);
    }

    @Override
    public void inject(T instance, CreationalContext<T> ctx) {
        injectionTarget.inject(instance, ctx);
        
    }

    @Override
    public void postConstruct(T instance) {
        injectionTarget.postConstruct(instance);
       registerBeanListener(instance, true);
       registerChannelListener(instance, true);
       
    }

    @Override
    public void preDestroy(T instance) {
        injectionTarget.preDestroy(instance);
        registerBeanListener(instance, false);
        registerChannelListener(instance, false);
       
    }
    
    private void registerChannelListener(T instance, boolean doRegister) {
        ChannelListener channelListener = AnnotationsUtils.getAnnotation(instance.getClass(), ChannelListener.class);
        if(channelListener != null) {
            if(doRegister) {
                register.registerChannelListener(channelListener, instance);
            } else {
                register.unregisterChannelListener(channelListener, instance);
            }
        }
    }
    
    private void registerBeanListener(T instance, boolean doRegister) {
        BeanListener beanListener = AnnotationsUtils.getAnnotation(instance.getClass(), BeanListener.class);
        if(beanListener != null) {
            if(doRegister) {
                register.registerBeanListener(beanListener, instance);
            } else {
                register.unregisterBeanListener(beanListener, instance);
            }
        }
    }
    
  
  
    
    
}
