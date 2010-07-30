package org.kasource.kaevent.core.event;

import java.util.EventObject;

/**
 * Optional base class to derive event object implementations from.
 * 
 * @author rikard
 * @version $Id$
 **/
public abstract class BaseEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private static long idCounter = 0;

    private final long creationTimestamp;
    private final String creatorThreadName;
    private final long id;

    public BaseEvent(Object source) {
        super(source);
        this.creationTimestamp = System.currentTimeMillis();
        this.creatorThreadName = Thread.currentThread().getName();
        this.id = ++idCounter; 
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getCreatorThreadName() {
        return creatorThreadName;
    }

    public long getId() {
        return id;
    }

}
