package org.kasource.kaevent.websocket.filter;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketEventType;

/**
 * Event Filter that only allows WebSocket event for client disconnect.
 * 
 * @author rikardwi
 **/
public class DisconnectionEventFilter implements EventFilter<WebSocketEvent> {

    @Override
    public Class<WebSocketEvent> handlesEvent() {
        return WebSocketEvent.class;
    }

    @Override
    public boolean passFilter(WebSocketEvent event) {
        return (event.getType() == WebSocketEventType.CLIENT_DISCONNECTED);
    }

}
