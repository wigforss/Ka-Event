package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;



public class DefaultSourceObjectListenerRegister implements SourceObjectListenerRegister{
	 private Map<Object, Map<EventListener,Object>> objectListeners = new WeakHashMap<Object, Map<EventListener,Object>>();
	 private Map<Class<? extends EventObject>, Map<Object, Map<EventListener,Object>>> objectListenersByEvent = new HashMap<Class<? extends EventObject>, Map<Object, Map<EventListener,Object>>>();
	 private EventRegister eventRegister;
	 
	 public DefaultSourceObjectListenerRegister(EventRegister eventRegister) {
		 this.eventRegister = eventRegister;
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
                EventConfig eventConfig = eventRegister.getEventByInterface(interfaceClass);
                if (eventConfig != null) {
                    Map<Object, Map<EventListener,Object>> sourceListeners = objectListenersByEvent.get(eventConfig
                            .getEventClass());
                    if (sourceListeners == null) {
                        sourceListeners = new WeakHashMap<Object, Map<EventListener,Object>>();
                        objectListenersByEvent.put(eventConfig.getEventClass(), sourceListeners);
                    }
                    Map<EventListener,Object> listeners = sourceListeners.get(sourceObject);
                    if (listeners == null) {
                        listeners = new WeakHashMap<EventListener,Object>();
                        sourceListeners.put(sourceObject, listeners);
                    }
                    listeners.put(listener,null);
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
                EventConfig eventConfig = eventRegister.getEventByInterface(interfaceClass);
                if (eventConfig != null) {
                    Map<Object, Map<EventListener,Object>> sourceListeners = objectListenersByEvent.get(eventConfig
                            .getEventClass());
                    if (sourceListeners != null) {
                        Map<EventListener,Object> listeners = sourceListeners.get(sourceObject);
                        if (listeners != null) {
                            listeners.remove(listener);
                        }
                
                    }
                }
            }
        }
    }

	@Override
	public Set<EventListener> getListenersByEvent(EventObject event) {
		Map<Object, Map<EventListener,Object>> sourceMap = objectListenersByEvent.get(event.getClass());
        if (sourceMap != null) {
            Map<EventListener,Object> objectListenerMap = sourceMap.get(event.getSource());
            if (objectListenerMap != null) {
               return objectListenerMap.keySet();
            }
        }
		return null;
	}
    
	
	

}
