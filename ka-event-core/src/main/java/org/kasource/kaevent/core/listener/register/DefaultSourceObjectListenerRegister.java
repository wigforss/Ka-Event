package org.kasource.kaevent.core.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.kenai.sadelf.event.config.EventConfig;
import com.kenai.sadelf.event.config.EventConfigFactory;
import com.kenai.sadelf.event.register.EventListenerRegistration;

public class DefaultSourceObjectListenerRegister implements SourceObjectListenerRegister{
	 private Map<Object, Map<EventListener,Object>> objectListeners = new WeakHashMap<Object, Map<EventListener,Object>>();
	 private Map<Class<? extends EventObject>, Map<Object, Map<EventListenerRegistration,Object>>> objectListenersByEvent = new HashMap<Class<? extends EventObject>, Map<Object, Map<EventListenerRegistration,Object>>>();
	 private EventConfigRegister eventConfigFactory;
	 
	 public DefaultSourceObjectListenerRegister(EventConfigRegister eventConfigFactory) {
		 this.eventConfigFactory = eventConfigFactory;
	 }
	 
	 /**
	  * Register a listener to listen on events from sourceObject
	  * 
	  * @param listener
	  *            Listener to register
	  * @param sourceObject
	  *            Source object to listen on
	  **/
	 @SuppressWarnings("unchecked")
	 public void registerListener(EventListener listener, Object sourceObject) {
        Map<EventListener,Object> listenerMap = objectListeners.get(sourceObject);
        if (listenerMap == null) {
            listenerMap = new WeakHashMap<EventListener,Object>();
            objectListeners.put(sourceObject, listenerMap);
        }
        listenerMap.put(listener,null);
      
        Class[] interfaceClasses = listener.getClass().getInterfaces();
        for (Class interfaceClass : interfaceClasses) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventConfigFactory.getEventByListener(interfaceClass);
                if (eventConfig != null) {
                    Map<Object, Map<EventListenerRegistration,Object>> sourceListeners = objectListenersByEvent.get(eventConfig
                            .getEventClass());
                    if (sourceListeners == null) {
                        sourceListeners = new WeakHashMap<Object, Map<EventListenerRegistration,Object>>();
                        objectListenersByEvent.put(eventConfig.getEventClass(), sourceListeners);
                    }
                    Map<EventListenerRegistration,Object> listeners = sourceListeners.get(sourceObject);
                    if (listeners == null) {
                        listeners = new WeakHashMap<EventListenerRegistration,Object>();
                        sourceListeners.put(sourceObject, listeners);
                    }
                    listeners.put(new EventListenerRegistration(listener, eventConfigFactory),null);
                }
            }
        }
    }
    
	 /**
	  * Unregister a listener to listen on events from sourceObject
	  * 
	  * @param listener
	  *            Listener to unregister
	  * @param sourceObject
	  *            Source object to unregister from
	  **/
	 @SuppressWarnings("unchecked")
	 public void unregisterListener(EventListener listener, Object sourceObject) {
		 Map<EventListener,Object> listenerMap = objectListeners.get(sourceObject);
		 if (listenerMap != null) {

			 objectListeners.remove(listener);
		 }   
        Class[] interfaceClasses = listener.getClass().getInterfaces();
        for (Class interfaceClass : interfaceClasses) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventConfigFactory.getEventByListener(interfaceClass);
                if (eventConfig != null) {
                    Map<Object, Map<EventListenerRegistration,Object>> sourceListeners = objectListenersByEvent.get(eventConfig
                            .getEventClass());
                    if (sourceListeners != null) {
                        Map<EventListenerRegistration,Object> listeners = sourceListeners.get(sourceObject);
                        if (listeners != null) {
                            listeners.remove(new EventListenerRegistration(listener,eventConfigFactory));
                        }
                
                    }
                }
            }
        }
    }

	@Override
	public Set<EventListenerRegistration> getListenersByEvent(EventObject event) {
		Map<Object, Map<EventListenerRegistration,Object>> sourceMap = objectListenersByEvent.get(event.getClass());
        if (sourceMap != null) {
            Map<EventListenerRegistration,Object> objectListenerMap = sourceMap.get(event.getSource());
            if (objectListenerMap != null) {
               return objectListenerMap.keySet();
            }
        }
		return null;
	}
    
	
	

}
