package org.kasource.kaevent.websocket.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.websocket.annotation.OnBinaryWebSocketMessage;
import org.kasource.web.websocket.WebSocket;

@Event(listener = WebSocketBinaryMessageListener.class, annotation = OnBinaryWebSocketMessage.class)
public class WebSocketBinaryMessageEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private byte[] message;
    private String clientId;
    
    public WebSocketBinaryMessageEvent(WebSocket socket, byte[] message, String clientId) {
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
    protected byte[] getMessage() {
        return message;
    }

  
    /**
     * @return the clientId
     */
    protected String getClientId() {
        return clientId;
    }

    
}
