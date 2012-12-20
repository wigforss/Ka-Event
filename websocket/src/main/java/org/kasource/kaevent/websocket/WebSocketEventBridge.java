package org.kasource.kaevent.websocket;

import javax.servlet.ServletContext;

import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventInitializedListener;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.websocket.annotation.OnWebSocketEvent;
import org.kasource.kaevent.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.kaevent.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.WebSocket;
import org.kasource.web.websocket.WebSocketEventListener;
import org.kasource.web.websocket.WebSocketFactory;

import org.kasource.web.websocket.WebSocketFactoryImpl;
import org.kasource.web.websocket.WebSocketMessageListener;
import org.kasource.web.websocket.event.WebSocketEvent;

public class WebSocketEventBridge implements KaEventInitializedListener, WebSocketEventListener, WebSocketMessageListener {

    private EventDispatcher eventDispatcher;
    private WebSocketFactoryImpl webSocketFactory = new WebSocketFactoryImpl();
    
    public WebSocketEventBridge(ServletContext context) throws Exception {
        KaEventInitializer.getInstance().addListener(this);
        webSocketFactory.initialize(context);
       
    }

    @Override
    public void doInitialize(KaEventConfiguration config) {
        eventDispatcher = config.getEventDispatcher();
        
        // Register events
        EventRegister eventRegister = config.getEventRegister();
        EventBuilderFactory eventFactory = config.getEventBuilderFactory();
      
        eventRegister.registerEvent(eventFactory.getBuilder(WebSocketBinaryMessageEvent.class).build());
       
        eventRegister.registerEvent(eventFactory.getBuilder(WebSocketTextMessageEvent.class).build());
     
        eventRegister.registerEvent(eventFactory.getBuilder(WebSocketEvent.class)
                    .bindInterface(WebSocketEventListener.class)
                    .bindAnnotation(OnWebSocketEvent.class)
                    .build());
        
        // Listen on all web sockets
        webSocketFactory.listenTo("*",(WebSocketEventListener) this);
        webSocketFactory.listenTo("*",(WebSocketMessageListener) this);
    }

    @Override
    public void onBinaryMessage(byte[] message, String clientId, WebSocket webSocket) {
        eventDispatcher.bridgeEvent(new WebSocketBinaryMessageEvent(webSocket, message, clientId));
        
    }

    @Override
    public void onMessage(String message, String clientId, WebSocket webSocket) {
        eventDispatcher.bridgeEvent(new WebSocketTextMessageEvent(webSocket, message, clientId));
        
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        eventDispatcher.bridgeEvent(event);
    }
    
    public WebSocketFactory getWebSocketFactory() {
        return webSocketFactory;
    }
}
