package com.example.chatg3.chat.configs;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collection;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private final ActiveUserManager activeUserManager;

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(ActiveUserManager activeUserManager, SimpMessagingTemplate messagingTemplate) {
        this.activeUserManager = activeUserManager;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
        String userId = sha.getFirstNativeHeader("userId");


        System.out.println("Connect info: Session: " + sessionId + " | userId: " + userId);
        if (userId != null) {
            activeUserManager.add(sessionId, userId);
            System.out.println("User connected: " + userId);
            broadcastConnectedUsers();
        } else {
            System.out.println("User id is null when storing on active users");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        String userId = activeUserManager.getUserId(sessionId);

        if (userId != null) {
            activeUserManager.remove(sessionId);
            System.out.println("User disconnected: " + userId);
            broadcastConnectedUsers();
        } else {
            System.out.println("User id is null, not found in the active users when disconnecting");
        }
    }


    private void broadcastConnectedUsers() {
        // Get all connected users
        Collection<String> connectedUsers = activeUserManager.getAllUsers().values();
        // Send connected user to topic "/topic/connectedUsers"
        messagingTemplate.convertAndSend("/topic/connectedUsers", connectedUsers);
    }


}
