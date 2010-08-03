/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;

/**
 * @author rikardwigforss
 *
 */
public interface EventExporter {


    public abstract Set<EventConfig> exportEvents() throws IOException;

}