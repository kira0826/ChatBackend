package com.example.chatg3.chat.configs;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ActiveUserManager {

    private ConcurrentMap<String, String> connectedUsers = new ConcurrentHashMap<>();


    public void add(String sessionId, String userId) {
        connectedUsers.put(sessionId, userId);
    }

    public void remove(String sessionId) {
        connectedUsers.remove(sessionId);
    }

    public ConcurrentMap<String, String> getAllUsers() {
        return connectedUsers;
    }

    public String getUserId(String sessionId) {
        return connectedUsers.get(sessionId);
    }



}
