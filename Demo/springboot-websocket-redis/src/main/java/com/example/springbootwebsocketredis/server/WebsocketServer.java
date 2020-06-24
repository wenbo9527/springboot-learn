package com.example.springbootwebsocketredis.server;

import com.alibaba.fastjson.JSON;
import com.example.springbootwebsocketredis.dao.MsgDao;
import com.example.springbootwebsocketredis.dao.UserDao;
import com.example.springbootwebsocketredis.entity.Msg;
import com.example.springbootwebsocketredis.entity.User;
import com.example.springbootwebsocketredis.listener.SubscribeListener;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    private static UserDao userDao;
    private static MsgDao msgDao;

    @Autowired
    public void setMsgDao(MsgDao msgDao){
        WebsocketServer.msgDao = msgDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao){
        WebsocketServer.userDao = userDao;
    }

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
    public void onOpen(Session session,@PathParam("topic") String topic,@PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        onlineUser.put(this, userId);
        //登录时订阅聊天室
        subscribeListener = new SubscribeListener();
        subscribeListener.setRedisTemplate(redisTemplate);
        subscribeListener.setSession(session);
        container.addMessageListener(subscribeListener, new ChannelTopic(topic));
        //将用户id存入redis
        HashOperations hashOperations = redisTemplate.opsForHash();
        //先判断用户是否存在
        Boolean exitUserId = hashOperations.hasKey("user", userId);
        if (!exitUserId) {//初次登陆时
            hashOperations.put("user", userId, "1");
            User user = new User();
            Date date = new Date();
            user.setLast_online_time(date);
            user.setLast_offline_time(date);
            user.setUsername(userId);
            asyncService.insertUser(user);
        } else{
            //获取离线消息
            //1.先获取用户接收到的最后一条消息
            //2.根据mid去筛选出用户未接收到的消息
            User user = userDao.findUserByUsername(userId);
            try {
                if (user.getLast_receive_msg_id() != 0) {//存在离线消息
                    //自动带出离线消息
                    List<Msg> msgs = msgDao.findOfflineMsgByUser(user.getLast_receive_msg_id());
                    //传递前台离线消息
                    session.getBasicRemote().sendText(JSON.toJSONString(msgs));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //异步保存数据
                Date date = new Date();
                asyncService.updateOnlineTimeUser(userId, date);
            }
        }
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
