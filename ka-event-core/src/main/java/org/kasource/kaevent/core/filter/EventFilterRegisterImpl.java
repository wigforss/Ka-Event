package org.kasource.kaevent.core.filter;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.core.bean.BeanResolver;
import org.kasource.kaevent.core.event.config.EventConfig;
import org.kasource.kaevent.core.event.register.EventRegister;
import org.kasource.kaevent.core.listener.implementations.EventListenerFilter;



public class EventFilterRegisterImpl implements EventFilterRegister {
    private Map<Class<? extends EventObject>, Set<EventFilter<? extends EventObject>>> filterMap = new HashMap<Class<? extends EventObject>, Set<EventFilter<? extends EventObject>>>();
    private Class<? extends EventListener> eventListener;
    private BeanResolver beanResolver;
    private EventRegister eventRegister;

    public EventFilterRegisterImpl(Class<? extends EventListener> eventListener, EventRegister eventRegister) {
        this.eventListener = eventListener;
        this.eventRegister = eventRegister;
        registerByAnnotations(eventListener);
    }

    public EventFilterRegisterImpl(Class<? extends EventListener> eventListener, EventRegister eventRegister,
            Collection<EventFilter<? extends EventObject>> eventFilters) {
        this.eventListener = eventListener;
        this.eventRegister = eventRegister;
        registerByAnnotations(eventListener);
        if (eventFilters != null) {
            for (EventFilter<? extends EventObject> filter : eventFilters) {
                registerFilter(filter);
            }
        }
    }

    @Override
    public void initialize() {

    }

    @SuppressWarnings("unchecked")
    private void registerByAnnotations(Class<? extends EventListener> eventListener) {
        EventListenerFilter annotation = eventListener.getAnnotation(EventListenerFilter.class);
        if (annotation != null) {
            for (String beanName : annotation.value()) {
                Object filterBean = beanResolver.getBean(beanName);
                try {
                    registerFilter((EventFilter<? extends EventObject>) filterBean);
                } catch (ClassCastException cce) {
                    throw new IllegalStateException("Bean with name " + beanName + " (" + filterBean.getClass()
                            + ") does not implement " + EventFilter.class);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EventObject> void registerFilter(EventFilter<T> filter) {
        Class<? extends EventObject> eventClass = (Class<? extends EventObject>) ((ParameterizedType) filter.getClass()
                .getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Class[] interfaces = eventListener.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventRegister
                        .getEventByInterface((Class<? extends EventListener>) interfaceClass);
                if (eventConfig != null) {
                    if (eventClass.isAssignableFrom(eventConfig.getEventClass())) {
                        addToFilterMap(eventConfig.getEventClass(), filter);
                    }
                }
            }
        }

    }

    private void addToFilterMap(Class<? extends EventObject> eventClass, EventFilter<? extends EventObject> filter) {
        Set<EventFilter<? extends EventObject>> filterSet = filterMap.get(eventClass);
        if (filterSet == null) {
            filterSet = new HashSet<EventFilter<? extends EventObject>>();
            filterMap.put(eventClass, filterSet);
        }
        filterSet.add(filter);
    }

    public Set<EventFilter<? extends EventObject>> getFilters(Class<? extends EventObject> eventClass) {
        return filterMap.get(eventClass);
    }
}
