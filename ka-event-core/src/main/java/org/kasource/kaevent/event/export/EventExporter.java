/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;

/**
 * @author rikardwigforss
 *
 */
public interface EventExporter {


    public abstract Set<EventConfig> exportEvents(EventFactory eventFactory) throws IOException;

}