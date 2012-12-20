package org.kasource.kaevent.websocket.event;

import java.util.EventListener;

public interface WebSocketTextMessageListener extends EventListener {
   
    public void onMessage(WebSocketTextMessageEvent event);
}
