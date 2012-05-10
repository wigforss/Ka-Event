package org.kasource.kaevent.event.dispatch;

import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class EventQueueFactoryBean implements FactoryBean, ApplicationContextAware {

    private String name;

    private Class<? extends DispatcherQueueThread> eventQueueClass;
   
    private DispatcherQueueThread eventQueueRef;

    private short maxThreads;
    
    private short coreThreads;
    
    private long keepAliveTime;
    
    private ApplicationContext applicationContext;
    
    @Override
    public DispatcherQueueThread getObject() throws Exception {
        DispatcherQueueThread eventQueue= null;
        if(eventQueueRef != null) {
            eventQueue =  eventQueueRef;
        } else {
            eventQueue = createEventQueue();
        }
        EventQueueRegister eventQueueRegister = 
            (EventQueueRegister) applicationContext.getBean(KaEventSpringBean.EVENT_QUEUE_REGISTER.getId());
        eventQueueRegister.registerEventQueue(name, eventQueue);
        return eventQueue;
    }

    private DispatcherQueueThread createEventQueue() {
        EventRouter eventRouter = 
            (EventRouter) applicationContext.getBean(KaEventSpringBean.EVENT_ROUTER.getId());
        DispatcherQueueThread eventQueue= null;
        if(eventQueueClass == null || eventQueueClass.equals(ThreadPoolQueueExecutor.class)) {
            eventQueue = new ThreadPoolQueueExecutor();
        } else {
           try {
            eventQueue = eventQueueClass.newInstance();
           } catch(Exception e) {
               throw new IllegalStateException("Could not create instance of " + eventQueueClass + " no empty public constructor found!");
           }
        }
        eventQueue.setEventRouter(eventRouter);
        if(maxThreads > 0) {
            eventQueue.setMaxThreads(maxThreads);
        }
        if(coreThreads > 0) {
            eventQueue.setCoreThreads(coreThreads);
        }
        if(keepAliveTime > 0) {
            eventQueue.setKeepAliveTime(keepAliveTime);
        }
        return eventQueue;
    }
    
    
    @Override
    public Class<?> getObjectType() {
        return DispatcherQueueThread.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    

    /**
     * @param eventQueueClass the eventQueueClass to set
     */
    public void setEventQueueClass(Class<? extends DispatcherQueueThread> eventQueueClass) {
        this.eventQueueClass = eventQueueClass;
    }

   
    /**
     * @param eventQueueRef the eventQueueRef to set
     */
    public void setEventQueueRef(DispatcherQueueThread eventQueueRef) {
        this.eventQueueRef = eventQueueRef;
    }

    /**
     * @param maxThreads the maxThreads to set
     */
    public void setMaxThreads(short maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * @param coreThreads the coreThreads to set
     */
    public void setCoreThreads(short coreThreads) {
        this.coreThreads = coreThreads;
    }

    /**
     * @param keepAliveTime the keepAliveTime to set
     */
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }



    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
