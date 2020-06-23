package com.example.springbootwebsocketredis.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.websocket.Session;
import java.io.IOException;

public class SubscribeListener implements MessageListener {

    private RedisTemplate redisTemplate;

    public Session getSession() {
        return session;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private Session session;

    @Override
    public void onMessage(Message message, byte[] bytes) {

        String msg = new String(message.getBody());
        if(null != session){
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
