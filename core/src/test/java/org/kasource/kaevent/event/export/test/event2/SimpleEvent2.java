/**
 * 
 */
package org.kasource.kaevent.event.export.test.event2;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;

/**
 * @author rikardwigforss
 *
 */
@SuppressWarnings("serial")
///CLOVER:OFF
//CHECKSTYLE:OFF
@Event(listener = SimpleEventListener2.class)
public class SimpleEvent2 extends EventObject {
    public SimpleEvent2() {
        super("Simple");
    }
}
