package com.example.chatg3.chat.configs;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class UserHandShakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String userId = request.getURI().getQuery(); // get the user id from the query that is put on the url

        System.out.println("User id on before handshake: " + userId);

        if (userId != null && !userId.isEmpty()) {
            attributes.put("userId", userId); // put the user id in the attributes to be used later

            System.out.println("User id on attributes: " + attributes.get("userId"));

            return true;
        }else {
            System.out.println("User id is empty or null on before handshake");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        // No need to implement this method

    }
}
