/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.config.KaEventConfig;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.config.InvalidEventConfigurationException;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.MethodResolverFactory;

/**
 * @author rikardwigforss
 *
 */
public class XmlConfigEventExporter implements EventExporter {

   private List<KaEventConfig.Events.Event> eventList;
   private BeanResolver beanResolver;
   
   public XmlConfigEventExporter(List<KaEventConfig.Events.Event> eventList, BeanResolver beanResolver) {
       this.eventList = eventList;
       this.beanResolver = beanResolver;
   }
    
    @Override
    public Set<EventConfig> exportEvents(EventConfigFactory eventFactory) throws IOException {
        Set<EventConfig> eventsFound = new HashSet<EventConfig>();
        if(eventList != null && !eventList.isEmpty()) {
            for(KaEventConfig.Events.Event event : eventList) {
                Class<? extends EventListener> interfaceClass = getInterfaceClass(event.getListenerInterface());
                Class<? extends EventObject> eventClass = getEventClass(event.getEventClass());
                if(!hasMethodResolver(event)){
                    Method eventMethod = getEventMethod(eventClass, interfaceClass);
                    eventsFound.add(eventFactory.newWithEventMethod(eventClass, interfaceClass, eventMethod, event.getName()));
                } else if(event.getAnnotationMethodResolver() != null){
                   eventsFound.add(eventFactory.newFromAnnotatedInterfaceClass(eventClass, interfaceClass, event.getName()));
                } else {
                    eventsFound.add(eventFactory.newWithMethodResolver(eventClass, interfaceClass, getMethodResolver(event, eventClass, interfaceClass), event.getName()));
                }
            }
        }
        return eventsFound;
    }
    
    
    private Method getEventMethod(Class<? extends EventObject> eventClass, Class<? extends EventListener> listenerInterface) {
        Set<Method> methods = ReflectionUtils.getDeclaredMethodsMatchingReturnType(listenerInterface, Void.TYPE, eventClass);
        if(methods.size() == 1) {
           return methods.iterator().next();
        } else if(methods.size() == 0) {
            throw new InvalidEventConfigurationException("No \"void\" method found in "+listenerInterface+" which handles "+eventClass+" events");
        } else {
            throw new InvalidEventConfigurationException("More than one method found in "+listenerInterface+" which handles "+eventClass+" events, specify a method resolver");
        }
    }
    
    @SuppressWarnings("unchecked")
    private Class<? extends EventListener>  getInterfaceClass(String className) {
        try {
            return (Class<? extends EventListener>) Class.forName(className);
        }catch(ClassCastException cce) {
            throw new InvalidEventConfigurationException("Listener Interface class "+className+" must extend java.util.EventListener!", cce);
        } catch (ClassNotFoundException cnfe) {
            throw new InvalidEventConfigurationException("Listener Interface class "+className+" could not be found!",cnfe);
        }
    }
    
    @SuppressWarnings("unchecked")
    private Class<? extends EventObject>  getEventClass(String className) {
        try {
            return (Class<? extends EventObject>) Class.forName(className);
        }catch(ClassCastException cce) {
            throw new InvalidEventConfigurationException("Event class "+className+" must extend java.util.EventObject!", cce);
        } catch (ClassNotFoundException cnfe) {
            throw new InvalidEventConfigurationException("Event class "+className+" could not be found!",cnfe);
        }
    }
    
    private boolean hasMethodResolver(KaEventConfig.Events.Event event) {
        return (event.getAnnotationMethodResolver() != null || event.getFactoryMethodResolver() != null || event.getBeanMethodResolver() != null || event.getSwitchMethodResolver() != null);
    }
    
    @SuppressWarnings("unchecked")
    private MethodResolver getMethodResolver(KaEventConfig.Events.Event event,  Class<? extends EventObject> eventClass, Class<? extends EventListener> interfaceClass) {
        if(event.getBeanMethodResolver() != null) {
            KaEventConfig.Events.Event.BeanMethodResolver beanMethodResolver = event.getBeanMethodResolver();
            return MethodResolverFactory.getFromBean(beanResolver, beanMethodResolver.getBean());
        }else if(event.getFactoryMethodResolver() != null){
            KaEventConfig.Events.Event.FactoryMethodResolver factoryMethodResolver = event.getFactoryMethodResolver();
            Class<?> factoryClass;
            try {
                factoryClass = Class.forName(factoryMethodResolver.getFactoryClass());
                return MethodResolverFactory.getFromFactoryMethod(factoryClass, factoryMethodResolver.getFactoryMethod(), factoryMethodResolver.getFactoryMethodArgument());
            } catch (ClassNotFoundException e) {
                throw new InvalidEventConfigurationException("Could not find the factoryClass!", e);
            }
        } else {
            Map<String, String> methodMap = new HashMap<String, String>();
            for(KaEventConfig.Events.Event.SwitchMethodResolver.Case keywordCase : event.getSwitchMethodResolver().getCase()) {
                methodMap.put(keywordCase.getValue(), keywordCase.getMethod());
            }
           return MethodResolverFactory.newKeywordSwitch(eventClass, interfaceClass, event.getSwitchMethodResolver().getKeywordMethod(), methodMap, event.getSwitchMethodResolver().getDefault().getMethod());
        }
    }

   
}
