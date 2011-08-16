package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;

/**
 * Creates and exports events.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface EventExporter {

	/**
	 * Returns a set of EventConfig created.
	 * 
	 * @param eventFactory Factory used when creating EventConfig objects.
	 * 
	 * @return Set of events created.
	 * 
	 * @throws IOException if exception occurs.
	 **/
    public abstract Set<EventConfig> exportEvents(EventFactory eventFactory) throws IOException;

}
