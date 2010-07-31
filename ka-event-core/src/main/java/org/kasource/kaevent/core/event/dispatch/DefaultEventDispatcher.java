/**
 * 
 */
package org.kasource.kaevent.core.event.dispatch;

import java.util.EventObject;
import java.util.Queue;

import org.kasource.kaevent.core.channel.ChannelRegister;
import org.kasource.kaevent.core.event.EventDispatcher;
import org.kasource.kaevent.core.listener.register.SourceObjectListenerRegister;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventDispatcher implements EventDispatcher{

    
    private ChannelRegister channelRegister;
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    private Queue<EventObject> eventQueue;
    
    @Override
    public void fire(EventObject event) {
       
        
    }

   
    @Override
    public void fireBlocked(EventObject event) {
    	eventQueue.add(event);
        
    }

  
    @Override
    public void fireOnCommit(EventObject event) {
        throw new IllegalStateException("Not implemented on "+this.getClass());
        
    }




 
    @Override
    public ChannelRegister getChannelRegister() { 
        return channelRegister;
    }


 
    @Override
    public SourceObjectListenerRegister getSourceObjectListenerRegister() {
        return sourceObjectListenerRegister;
    }

}
