/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.kaevent.event.register.EventRegister;

/**
 * @author rikardwigforss
 *
 */
public class SourceObjectListenerRegisterImpl extends AbstractEventListenerRegister implements SourceObjectListenerRegister {
    private Map<Class<? extends EventObject>, Map<Object, Map<EventListener,Object>>> objectListenersByEvent = new HashMap<Class<? extends EventObject>, Map<Object, Map<EventListener,Object>>>();
    
    
    
    public SourceObjectListenerRegisterImpl(EventRegister eventRegister) {
        super(eventRegister);
    }
    
  
    @Override
    public Set<EventListener> getListenersByEvent(EventObject event) {
        Map<Object, Map<EventListener,Object>> objectListenerMap = objectListenersByEvent.get(event.getClass());
        if(objectListenerMap != null) {
            Map<EventListener, Object> listenerByObjectMap = objectListenerMap.get(event.getSource());
            if(listenerByObjectMap != null) {
                return listenerByObjectMap.keySet();
            }
        }
        return EMPTY_LISTENER_SET;
    }

 
    
    
    @Override
    protected void addListener(EventListener listener, Class<? extends EventObject> eventClass, Object sourceObject) {
        Map<Object, Map<EventListener, Object>> objectListenerMap = objectListenersByEvent.get(eventClass);
        if(objectListenerMap == null) {
            objectListenerMap = new WeakHashMap<Object, Map<EventListener,Object>>();
            objectListenersByEvent.put(eventClass, objectListenerMap);
        }
        Map<EventListener, Object> listenerByObjectMap = objectListenerMap.get(sourceObject);
        if(listenerByObjectMap == null) {
            listenerByObjectMap = new WeakHashMap<EventListener,Object>();
            objectListenerMap.put(sourceObject, listenerByObjectMap);
        }
        listenerByObjectMap.put(listener, null);
    }
    
    @Override
    public void unregisterListener(EventListener listener, Object sourceObject) {
     
        for(Map<Object, Map<EventListener,Object>> objectListenerMap : objectListenersByEvent.values()) {
            Map<EventListener, Object> listenerByObjectMap = objectListenerMap.get(sourceObject);
            if(listenerByObjectMap != null) {
                listenerByObjectMap.remove(listener);
            }
        }

    }

}
