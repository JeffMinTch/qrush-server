//package com.app.qrush.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(payload);
//        String matcherId = jsonNode.get("matcherId").asText();
//        String matcheeId = jsonNode.get("matcheeId").asText();
//        String destination = "/topic/" + matcheeId;
//        messagingTemplate.convertAndSend(destination, matcherId);
//    }
//}