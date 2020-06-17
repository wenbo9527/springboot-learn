package com.example.springbootwebsocketredis.server;

import com.example.springbootwebsocketredis.listener.SubscribeListener;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/websocket/{topic}/{userId}")
public class WebsocketServer {

    static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

    //聊天室登录人员
    private static ConcurrentHashMap<WebsocketServer,String> onlineUser = new ConcurrentHashMap<>();

    private static RedisTemplate redisTemplate;
    private static RedisMessageListenerContainer container;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        WebsocketServer.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setRedisMessageListenerContainer(RedisMessageListenerContainer container){
        WebsocketServer.container = container;
    }

    //进入聊天室人员的userid
    private String userId;

    //每个websocket客户端和服务器的会话
    private Session session;

    @OnOpen
    public void onOpen(Session session,@PathParam("topic") String topic,@PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        onlineUser.put(this,userId);
        //订阅
        SubscribeListener subscribeListener = new SubscribeListener();
        subscribeListener.setRedisTemplate(redisTemplate);
        subscribeListener.setSession(session);
        container.addMessageListener(subscribeListener,new ChannelTopic(topic));
    }

    @OnClose
    public void onClose(){
        onlineUser.remove(this);
    }

    @OnMessage
    public void onMessage(Session session,String message,@PathParam("topic") String topic,@PathParam("userId") String userId){
        message = userId + ":" + message;
        redisTemplate.convertAndSend(topic,message);
    }

}
