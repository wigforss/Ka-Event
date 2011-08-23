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
    
    /**
     * Constructor.
     * 
     * @param scanPath Classpath (package name) to scan for @Event annotated classes. 
     *                 Can be a comma separated list of package names.
     */
    public AnnotationEventExporter(String scanPath) {
        this.scanPath = scanPath;
    }
   
   
    /**
     * Returns all events found by scanning the classpath for classes
     * annotated with @Event.
     * 
     * @param eventFactory    Factory used to create EventConfig instances with.
     * 
     * @return Events found.
     * @throws IOException if exception occurs.
     */
    public Set<EventConfig> exportEvents(EventFactory eventFactory) throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        if (scanPath.contains(".")) {
            scanPath = scanPath.replace('.', '/');
        }     
        String[] scanPaths = scanPath.split(",");
        
        String includeRegExp = buildIncludeRegExp(scanPaths);
        Set<URL> urls = resolverUrls(scanPaths);
         
        Map<String, Set<String>> annotationIndex = scanForAnnotatedClasses(urls);
        Set<String> eventClassNames = annotationIndex.get(Event.class.getName());
        if (eventClassNames != null) {
            for (String eventClassName : eventClassNames) {
                addEvent(eventFactory, eventsFound, includeRegExp, eventClassName);
            }
        }

        return eventsFound;

    }

    /**
     * Created and adds the event to the eventsFound set, if its package matches the includeRegExp.
     * 
     * @param eventFactory      Event Factory used to create the EventConfig instance with.
     * @param eventsFound       Set of events found, to add the newly created EventConfig to.
     * @param includeRegExp     Regular expression to test eventClassName with.
     * @param eventClassName    Name of the class.
     **/
    @SuppressWarnings("unchecked")
    private void addEvent(EventFactory eventFactory, 
                         Set<EventConfig> eventsFound, 
                         String includeRegExp,
                         String eventClassName) {
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

    /**
     * Returns the URLs which has classes from scanPaths.
     * 
     * @param scanPaths Comma separated list of package names.
     * 
     * @return URLs matching classes in scanPaths.
     **/
    private Set<URL> resolverUrls(String[] scanPaths) {
       
        Set<URL> urls = new HashSet<URL>();
        for (String path : scanPaths) {
            URL[] urlsFromPath = ClasspathUrlFinder.findResourceBases(path.trim());
            urls.addAll(Arrays.asList(urlsFromPath));
            
        }
        
        return urls;
    }
    
    /**
     * Returns a regular expression of acceptable package names. 
     * 
     * @param scanPaths  Comma separated list of package names.
     * @return a regular expression of acceptable package names. 
     **/
    private String buildIncludeRegExp(String[] scanPaths) {
        String includeRegExp = "";
        for (String path : scanPaths) {
            includeRegExp += path.trim().replace("/", "\\.") + ".*|";
        }
        if (scanPaths.length > 0) {
            // Remove last |
            includeRegExp = includeRegExp.substring(0, includeRegExp.length() - 1);
        }
        return includeRegExp;
    }
    
    /**
     * Scans for classes in URLs.
     * 
     * @param urls URLs to scan for annotated classes.
     * 
     * @return Map of annotated classes found.
     * @throws IOException If exception occurs.
     **/
    private Map<String, Set<String>> scanForAnnotatedClasses(Set<URL> urls) throws IOException {
        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);
        
        db.scanArchives(urls.toArray(new URL[urls.size()]));
        return db.getAnnotationIndex();
        
    }

}
