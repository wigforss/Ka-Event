/**
 * 
 */
package org.kasource.kaevent.event.export.test.event;

import java.util.EventListener;

/**
 * @author rikardwigforss
 *
 */
public interface SimpleEventListener extends EventListener{

    public void onSimpleEvent(SimpleEvent event);
}
