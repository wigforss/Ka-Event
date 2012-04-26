/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.filter.methods.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.methods.MethodFilterList;
import org.kasource.commons.reflection.filter.methods.ReturnTypeMethodFilter;
import org.kasource.commons.reflection.filter.methods.SignatureMethodFilter;
import org.kasource.commons.util.reflection.ClassUtils;
import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.config.KaEventConfig;
import org.kasource.kaevent.event.config.EventBuilder;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.InvalidEventConfigurationException;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.MethodResolverFactory;

/**
 * Exports event configurations from a XML configuration object.
 * 
 * 
 * @author Rikard Wigforss
 * @version $Id$
 **/
public class XmlConfigEventExporter implements EventExporter {

   private List<KaEventConfig.Events.Event> eventList;
   private BeanResolver beanResolver;
   
   /**
    * Constructor.
    * 
    * @param eventList     List of XML Configuration Elements.
    * @param beanResolver  Bean Resolver.
    */
   public XmlConfigEventExporter(List<KaEventConfig.Events.Event> eventList, BeanResolver beanResolver) {
       this.eventList = eventList;
       this.beanResolver = beanResolver;
   }
    
   /**
    * Returns all events found in the eventList.
    * 
    * @param eventBuilderFactory    Factory used to create EventConfig instances with.
    * 
    * @return Events found.
    * @throws IOException if exception occurs.
    */
	@Override
    public Set<EventConfig> exportEvents(EventBuilderFactory eventBuilderFactory) throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        if (eventList != null && !eventList.isEmpty()) {
            for (KaEventConfig.Events.Event event : eventList) {
                addEvent(eventBuilderFactory, eventsFound, event);
            }
        }
        return eventsFound;
    }

	/**
	 * Creates and adds the event to the events found set.
	 * 
	 * @param eventBuilderFactory Factory to use when creating the EventConfig instance.
	 * @param eventsFound  Set of events to add the newly created event to.
	 * @param event        XML Configuration element.
	 **/
    private void addEvent(EventBuilderFactory eventBuilderFactory, Set<EventConfig> eventsFound, KaEventConfig.Events.Event event) {
	    Class<? extends EventListener> interfaceClass = null;
	    if(event.getListenerInterface() != null) {
	        interfaceClass = 
	            ClassUtils.getInterfaceClass(event.getListenerInterface(), EventListener.class);
	    }
	    Class<? extends Annotation> annotationClass = null;
	    if(event.getAnnotation() != null) {
	        annotationClass = ClassUtils.getClass(event.getAnnotation(), Annotation.class);
	    }
        Class<? extends EventObject> eventClass = 
            ClassUtils.getClass(event.getEventClass(), EventObject.class);
        
        EventBuilder eventBuilder = eventBuilderFactory.getBuilder(eventClass).name(event.getName());
        
        if(interfaceClass != null) {
            if(!hasMethodResolver(event)) {
                eventBuilder = eventBuilder.bindInterface(interfaceClass, getEventMethod(eventClass, interfaceClass));
            } else {
                eventBuilder = eventBuilder.bindInterface(interfaceClass, getMethodResolver(event, 
                            eventClass, 
                            interfaceClass));
            }
        }
        if(annotationClass != null) {
            eventBuilder = eventBuilder.bindAnnotation(annotationClass);
        }
       
        eventsFound.add(eventBuilder.build());
    }
    
    /**
     * Returns the event method to use.
     * 
     * The listenerInterface should have one void method taking the the eventClass
     * as the only parameter. That method will be returned.
     * 
     * @param eventClass        Event Class
     * @param listenerInterface Event Listener Interface Class.
     * 
     * @return Returns the event method to invoke.
     * @throws InvalidEventConfigurationException if no applicable method was found or more than one exists.
     **/
    private Method getEventMethod(Class<? extends EventObject> eventClass, 
                                  Class<? extends EventListener> listenerInterface) 
        throws InvalidEventConfigurationException {
        Set<Method> methods = 
            MethodUtils.getDeclaredMethods(listenerInterface, new MethodFilterBuilder().hasReturnType(Void.TYPE).hasSignature(eventClass).build());
            
        if (methods.size() == 1) {
           return methods.iterator().next();
        } else if (methods.size() == 0) {
            throw new InvalidEventConfigurationException("No \"void\" method found in "
                        + listenerInterface + " which handles " + eventClass + " events");
        } else {
            throw new InvalidEventConfigurationException("More than one method found in "
                        + listenerInterface + " which handles " + eventClass + " events, specify a method resolver");
        }
    }
    
    /**
     * Returns true if The XML Configuration element event has a
     * configured Method Resolver, else false.
     * 
     * @param event XML Configuration element to inspect.
     * 
     * @return true if method resolver is configured.
     **/
    private boolean hasMethodResolver(KaEventConfig.Events.Event event) {
        return (event.getAnnotationMethodResolver() != null 
                    || event.getFactoryMethodResolver() != null 
                    || event.getBeanMethodResolver() != null 
                    || event.getSwitchMethodResolver() != null);
    }
    
    /**
     * Returns the method resolver for the XML Configuration element event.
     * 
     * @param event           The XML Configuration Element to use.
     * @param eventClass      The actual Event Class.
     * @param interfaceClass  The Event Listener Interface.
     * 
     * @return Method resolver found.
     */
    @SuppressWarnings("rawtypes")
    private MethodResolver getMethodResolver(KaEventConfig.Events.Event event,  
                                             Class<? extends EventObject> eventClass, 
                                             Class<? extends EventListener> interfaceClass) {
        
        if (event.getBeanMethodResolver() != null) {
            KaEventConfig.Events.Event.BeanMethodResolver beanMethodResolver = event.getBeanMethodResolver();
            return MethodResolverFactory.getFromBean(beanResolver, beanMethodResolver.getBean());
        } else if (event.getFactoryMethodResolver() != null) {
            KaEventConfig.Events.Event.FactoryMethodResolver factoryMethodResolver = event.getFactoryMethodResolver();
            Class<?> factoryClass;
            try {
                factoryClass = Class.forName(factoryMethodResolver.getFactoryClass());
                return MethodResolverFactory.getFromFactoryMethod(factoryClass, 
                                                                  factoryMethodResolver.getFactoryMethod(), 
                                                                  factoryMethodResolver.getFactoryMethodArgument());
            } catch (ClassNotFoundException e) {
                throw new InvalidEventConfigurationException("Could not find the factoryClass!", e);
            }
        } else {
            Map<String, String> methodMap = new HashMap<String, String>();
            for (KaEventConfig.Events.Event.SwitchMethodResolver.Case keywordCase 
                        : event.getSwitchMethodResolver().getCase()) {
                methodMap.put(keywordCase.getValue(), keywordCase.getMethod());
            }
           return MethodResolverFactory.newKeywordSwitch(eventClass, 
                                                         interfaceClass, 
                                                         event.getSwitchMethodResolver().getKeywordMethod(), 
                                                         methodMap, 
                                                         event.getSwitchMethodResolver().getDefault().getMethod());
        }
    }

   
}
