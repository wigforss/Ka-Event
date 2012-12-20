package org.kasource.kaevent.websocket.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.websocket.annotation.OnTextWebSocketMessage;
import org.kasource.web.websocket.WebSocket;

@Event(listener = WebSocketTextMessageListener.class, annotation = OnTextWebSocketMessage.class)
public class WebSocketTextMessageEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private String message;
    private String clientId;
    
    public WebSocketTextMessageEvent(WebSocket socket, String message, String clientId) {
        super(socket);
        this.message = message;
        this.clientId = clientId;
      
    }
    
    @Override
    public WebSocket getSource() {
        return (WebSocket) super.getSource();
    }

    /**
     * @return the message
     */
    protected String getMessage() {
        return message;
    }

    

    /**
     * @return the clientId
     */
    protected String getClientId() {
        return clientId;
    }

    
    
}
