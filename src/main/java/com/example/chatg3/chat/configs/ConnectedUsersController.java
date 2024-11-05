package com.example.chatg3.chat.configs;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class ConnectedUsersController {


    private final ActiveUserManager activeUserManager;
    private final SimpMessagingTemplate messagingTemplate;

    public ConnectedUsersController(ActiveUserManager activeUserManager, SimpMessagingTemplate messagingTemplate) {
        this.activeUserManager = activeUserManager;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/requestConnectedUsers")
    public void sendConnectedUsersList() {
        Collection<String> connectedUsers = activeUserManager.getAllUsers().values();
        // Enviar la lista al topic /topic/connectedUsers
        System.out.println("Sending connected users list from ConnectedUsersController");
        System.out.println("List: " + connectedUsers);

        messagingTemplate.convertAndSend("/topic/connectedUsers", connectedUsers);
    }
}
