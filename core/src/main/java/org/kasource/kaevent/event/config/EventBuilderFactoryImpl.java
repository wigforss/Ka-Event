package org.kasource.kaevent.event.config;

import java.util.EventObject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;

/**
 * Default implementation of EventBuilderFactory, used to create EventBuilder objects,
 * which can be used to build events with.
 * 
 * @author Rikard Wigforss
 * @version $Id: $
 */
public class EventBuilderFactoryImpl implements EventBuilderFactory {

    private BeanResolver beanResolver;
    private EventQueueRegister eventQueueRegister;
 

    /**
     * Constructor.
     * 
     * @param beanResolver
     *            Bean resolver to use.
     **/
    public EventBuilderFactoryImpl(BeanResolver beanResolver, EventQueueRegister eventQueueRegister) {
        this.beanResolver = beanResolver;
        this.eventQueueRegister = eventQueueRegister;
    }

    /**
     * Returns a new builder for eventClass.
     * 
     * @param eventClass    Event class to create builder for.
     *
     * @return Builder which can build event.
     **/
    public EventBuilder getBuilder(Class<? extends EventObject> eventClass) {
        return new EventBuilderImpl(beanResolver, eventQueueRegister, eventClass);
    }
    

}
