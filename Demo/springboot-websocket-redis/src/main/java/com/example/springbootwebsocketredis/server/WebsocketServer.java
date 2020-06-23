package com.example.springbootwebsocketredis.server;

import com.example.springbootwebsocketredis.listener.SubscribeListener;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/websocket/{topic}/{userId}")
public class WebsocketServer {

    static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

    //聊天室登录人员
    private static ConcurrentHashMap<WebsocketServer,String> onlineUser = new ConcurrentHashMap<>();

    private static RedisTemplate redisTemplate;
    private static RedisMessageListenerContainer container;
    private static AsyncService asyncService;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        WebsocketServer.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setRedisMessageListenerContainer(RedisMessageListenerContainer container){
        WebsocketServer.container = container;
    }

    @Autowired
    public void setAsyncService(AsyncService asyncService){
        WebsocketServer.asyncService = asyncService;
    }

    //进入聊天室人员的userid
    private String userId;

    //每个websocket客户端和服务器的会话
    private Session session;

    //监听器
    private SubscribeListener subscribeListener;

    @OnOpen
    public void onOpen(Session session,@PathParam("topic") String topic,@PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        onlineUser.put(this,userId);
        //登录时订阅聊天室
        subscribeListener = new SubscribeListener();
        subscribeListener.setRedisTemplate(redisTemplate);
        subscribeListener.setSession(session);
        container.addMessageListener(subscribeListener,new ChannelTopic(topic));
        //异步保存数据
        Date date = new Date();
        asyncService.updateOnlineTimeUser(userId,date);
        //自动带出离线消息
        //将用户id存入redis
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("user",userId);
    }

    @OnClose
    public void onClose(){
        onlineUser.remove(this);
        container.removeMessageListener(this.subscribeListener);
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.remove("user",0,this.userId);
        //下线的时候异步将用户名称添加并且修改状态
        listOperations.leftPush("offuser",this.userId);
    }

    @OnMessage
    public void onMessage(Session session,String message,@PathParam("topic") String topic,@PathParam("userId") String userId){
        message = userId + ":" + message;
        asyncService.insertMsg(message);
        redisTemplate.convertAndSend(topic,message);
    }

}
