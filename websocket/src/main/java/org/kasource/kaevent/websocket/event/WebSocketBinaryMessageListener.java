package org.kasource.kaevent.websocket.event;

import java.util.EventListener;

public interface WebSocketBinaryMessageListener extends EventListener {
    
    public void onBinaryMessage(WebSocketBinaryMessageEvent event);
}
