package org.kasource.kaevent.websocket.filter;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketEventType;

/**
 * Event Filter that only allows client connect events.
 * 
 * @author rikardwi
 **/
public class ConnectionEventFilter implements EventFilter<WebSocketEvent> {

    @Override
    public Class<WebSocketEvent> handlesEvent() {
        return WebSocketEvent.class;
    }

    @Override
    public boolean passFilter(WebSocketEvent event) {
       return (event.getType() == WebSocketEventType.CLIENT_CONNECTED);
    }

}
