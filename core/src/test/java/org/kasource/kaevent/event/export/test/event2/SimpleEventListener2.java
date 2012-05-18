/**
 * 
 */
package org.kasource.kaevent.event.export.test.event2;

import java.util.EventListener;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
public interface SimpleEventListener2 extends EventListener {

    public void onSimpleEvent(SimpleEvent2 event);
}
