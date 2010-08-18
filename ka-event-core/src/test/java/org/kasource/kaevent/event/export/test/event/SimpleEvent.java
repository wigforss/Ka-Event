/**
 * 
 */
package org.kasource.kaevent.event.export.test.event;

import java.util.EventObject;

import org.kasource.kaevent.event.Event;

/**
 * @author rikardwigforss
 *
 */
@SuppressWarnings("serial")
///CLOVER:OFF
@Event(listener=SimpleEventListener.class)
public class SimpleEvent extends EventObject{
    public SimpleEvent() {
        super("Simple");
    }
}
