/**
 * 
 */
package org.kasource.kaevent.event.export.test.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;

/**
 * @author rikardwigforss
 *
 */
@SuppressWarnings("serial")
///CLOVER:OFF
//CHECKSTYLE:OFF
@Event(listener = SimpleEventListener.class)
public class SimpleEvent extends EventObject {
    public SimpleEvent() {
        super("Simple");
    }
}
