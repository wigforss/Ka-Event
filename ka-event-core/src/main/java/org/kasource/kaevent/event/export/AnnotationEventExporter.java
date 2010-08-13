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
public class AnnotationEventExporter implements EventExporter {
    public static final Logger logger = Logger.getLogger(AnnotationEventExporter.class);

   
    private AnnotationDB db = new AnnotationDB();
    private String scanPath;
    
    public AnnotationEventExporter(String scanPath) {
        this.scanPath = scanPath;
    }
   
   
    @SuppressWarnings("unchecked")
    public Set<EventConfig> exportEvents(EventConfigFactory eventConfigFactory) throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        if(scanPath.contains(".")) {
            scanPath = scanPath.replace('.', '/');
        }
        URL[] urls = ClasspathUrlFinder.findResourceBases(scanPath);
        String classPath = scanPath.replace('/', '.');
        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);

        db.scanArchives(urls);
        Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
        Set<String> eventClassNames = annotationIndex.get(Event.class.getName());
        if (eventClassNames != null) {
            for (String eventClassName : eventClassNames) {
                if(eventClassName.startsWith(classPath)) {
                try {
                    Class<?> eventClass = Class.forName(eventClassName);
                    eventClass.asSubclass(EventObject.class);
                    EventConfig eventConfig = eventConfigFactory.newFromAnnotatedEventClass(
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
        }

        return eventsFound;

    }

}
