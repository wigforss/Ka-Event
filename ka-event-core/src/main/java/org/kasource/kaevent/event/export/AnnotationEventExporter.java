/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.config.InvalidEventConfigurationException;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * 
 * Finds any class in scanPath that is annotated with @Event and exports them
 * 
 * @author rikardwigforss
 * 
 */
public class AnnotationEventExporter {
    public static final Logger logger = Logger.getLogger(AnnotationEventExporter.class);

    private String scanPath;
    private AnnotationDB db = new AnnotationDB();
    private EventConfigFactory eventConfigFactory;

    @SuppressWarnings("unchecked")
    public Set<EventConfig> exportEvents() throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        URL[] urls = ClasspathUrlFinder.findResourceBases(scanPath);

        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);

        db.scanArchives(urls);
        Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
        Set<String> eventClassNames = annotationIndex.get(Event.class.getName());
        if (eventClassNames != null) {
            for (String eventClassName : eventClassNames) {

                try {
                    Class<?> eventClass = Class.forName(eventClassName);
                    eventClass.asSubclass(EventObject.class);
                    EventConfig eventConfig = eventConfigFactory.createEventConfig(
                            (Class<? extends EventObject>) eventClass);
                    eventsFound.add(eventConfig);
                } catch (ClassNotFoundException cnfe) {
                    logger.error("Scannotation found a class that does not exist " + eventClassName + " !", cnfe);
                } catch (ClassCastException cce) {
                    throw new InvalidEventConfigurationException("Class " + eventClassName
                            + " is annoted with @Event but does not extend java.util.EventObject!");
                }
            }
        }

        return eventsFound;

    }

}
