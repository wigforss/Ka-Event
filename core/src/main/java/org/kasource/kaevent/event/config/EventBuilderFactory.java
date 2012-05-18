package org.kasource.kaevent.event.config;

import java.util.EventObject;

/**
 * Factory used to create EventConfig with.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface EventBuilderFactory {

    /**
     * Returns a new builder for eventClass.
     * 
     * @param eventClass    Event class to create builder for.
     *
     * @return Builder which can build event.
     **/
    public EventBuilder getBuilder(Class<? extends EventObject> eventClass);
}
