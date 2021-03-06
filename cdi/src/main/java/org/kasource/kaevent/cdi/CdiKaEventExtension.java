package org.kasource.kaevent.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.kasource.commons.util.reflection.AnnotationsUtils;
import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.event.EventDispatcher;

/**
 * Can observe
 * 
 * BeforeBeanDiscovery
 * ProcessAnnotatedType
 * ProcessInjectionTarget and ProcessProducer
 * ProcessBean and ProcessObserverMethod 
 * AfterBeanDiscovery
 * AfterDeploymentValidation
 *  
 * @author rikardwi
 **/
public class CdiKaEventExtension implements Extension {
    
    
    
    /**
     * Makes sure the @BeanListener and @ChannelListener instances are automatically registered
     * as listeners.
     * 
     * @param pit ProcessInjectionTarget to inspect.
     **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void processInjectorTarger(@Observes ProcessInjectionTarget<Object> pit) {
        Class<?> clazz = pit.getAnnotatedType().getJavaClass();
       
        if(AnnotationsUtils.isAnnotationPresent(clazz, BeanListener.class)
                    || AnnotationsUtils.isAnnotationPresent(clazz, ChannelListener.class)) {
            pit.setInjectionTarget(new RegisterEventListenerInjectionTarget(pit.getInjectionTarget()));         
        }
       
    }

    
    /**
     * Eagerly loads the Event Dispatcher.
     * 
     * @param event             AfterBeanDiscovery event.
     * @param eventDispatcher   Event Dispatcher to eagerly load.
     */
    void afterAfterDeploymentValidation(@Observes AfterDeploymentValidation event, EventDispatcher eventDispatcher) {
      eventDispatcher.toString();
    }


    
}
