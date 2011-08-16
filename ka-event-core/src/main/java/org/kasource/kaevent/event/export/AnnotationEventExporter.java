/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.InvalidEventConfigurationException;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * Finds any class in scanPath that is annotated with @Event and exports them.
 * 
 * @author rikardwigforss
 * @version $Id$ 
 **/
public class AnnotationEventExporter implements EventExporter {
    private static final Logger LOG = Logger.getLogger(AnnotationEventExporter.class);

   
    private AnnotationDB db = new AnnotationDB();
    private String scanPath;
    
    public AnnotationEventExporter(String scanPath) {
        this.scanPath = scanPath;
    }
   
   
    @SuppressWarnings("unchecked")
    public Set<EventConfig> exportEvents(EventFactory eventFactory) throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        if (scanPath.contains(".")) {
            scanPath = scanPath.replace('.', '/');
        }
        
        String includeRegExp = "";
        
        String[] scanPaths = scanPath.split(",");
        Set<URL> urls = new HashSet<URL>();
        for (String path : scanPaths) {
        	URL[] urlsFromPath = ClasspathUrlFinder.findResourceBases(path.trim());
        	urls.addAll(Arrays.asList(urlsFromPath));
        	includeRegExp += path.trim().replace("/", "\\.") + ".*|";
        }
        if (scanPaths.length > 0) {
        	// Remove last |
        	includeRegExp = includeRegExp.substring(0, includeRegExp.length() - 1);
        }
     
        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);
        
        db.scanArchives(urls.toArray(new URL[urls.size()]));
        Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
        Set<String> eventClassNames = annotationIndex.get(Event.class.getName());
        if (eventClassNames != null) {
            for (String eventClassName : eventClassNames) {
                if (eventClassName.matches(includeRegExp)) {
                try {
                    Class<?> eventClass = Class.forName(eventClassName);
                    eventClass.asSubclass(EventObject.class);
                    EventConfig eventConfig = eventFactory.newFromAnnotatedEventClass(
                            (Class<? extends EventObject>) eventClass);
                    eventsFound.add(eventConfig);
                } catch (ClassNotFoundException cnfe) {
                    LOG.error("Scannotation found a class that does not exist " + eventClassName + " !", cnfe);
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
