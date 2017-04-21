package com.mltrading.web.websocket;

import com.mltrading.models.stock.StockSector;
import com.mltrading.models.stock.cache.CacheStockSector;

import com.mltrading.web.websocket.dto.ActivityDTO;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;


import javax.inject.Inject;
import java.security.Principal;
import java.util.ArrayList;

import java.util.List;




@Controller
public class RealTimeWS  {

    private static final Logger log = LoggerFactory.getLogger(RealTimeWS.class);

    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @SubscribeMapping("/topic/rtevent")
    @SendTo("/topic/sectors")
    public List<StockSector> sendSectors(StompHeaderAccessor stompHeaderAccessor, Principal principal) {

        List<StockSector> l = new ArrayList<>(CacheStockSector.getSectorCache().values());
        return l;
    }


    public void onRealTimeWS() {
        List<StockSector> l = new ArrayList<>(CacheStockSector.getSectorCache().values());
        messagingTemplate.convertAndSend("/topic/sectors", l);
    }

}
