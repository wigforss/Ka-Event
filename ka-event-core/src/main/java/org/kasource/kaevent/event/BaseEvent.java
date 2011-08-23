package org.kasource.kaevent.event;

import java.util.EventObject;

/**
 * Optional base class to derive event object implementations from.
 * 
 * Will automatically add information on creation time stamp, thread and id.
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

    /**
     * Constructor.
     * 
     * @param source Source object.
     **/
    public BaseEvent(Object source) {
        super(source);
        this.creationTimestamp = System.currentTimeMillis();
        this.creatorThreadName = Thread.currentThread().getName();
        this.id = ++idCounter; 
    }

    /**
     * Returns the creation time stamp.
     * 
     * @return creation time stamp
     **/
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Returns the creation thread name.
     * 
     * @return creation thread name
     **/
    public String getCreatorThreadName() {
        return creatorThreadName;
    }

    /**
     * Returns the event ID.
     * 
     * @return event ID.
     **/
    public long getId() {
        return id;
    }

}
