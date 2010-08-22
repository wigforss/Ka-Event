/**
 * 
 */
package org.kasource.kaevent.listener.register;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rikardwigforss
 *
 */
@Component("sourceObjectListenerRegister")
public class SpringSourceObjectListenerRegister extends SourceObjectListenerRegisterImpl{

    /**
     * @param eventRegister
     * @param beanResolver
     */
    @Autowired
    public SpringSourceObjectListenerRegister(EventRegister eventRegister, BeanResolver beanResolver) {
        super(eventRegister, beanResolver);
    }

}
