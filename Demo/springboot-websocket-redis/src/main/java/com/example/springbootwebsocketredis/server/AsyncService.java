package com.example.springbootwebsocketredis.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springbootwebsocketredis.dao.MsgDao;
import com.example.springbootwebsocketredis.dao.UserDao;
import com.example.springbootwebsocketredis.entity.Msg;
import com.example.springbootwebsocketredis.entity.User;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncService{

    private static Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private MsgDao msgDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 异步插入消息
     * @param msgInfo
     */
    @Async("asyncServiceExecutor")
    public void insertMsg(String msgInfo){
        logger.info("线程-" + Thread.currentThread().getId() + "正在执行插入语句");
        //转换成json
//        JSONObject msgJson = JSON.parseObject(msgInfo);
        Date date = new Date();
        Msg msg = new Msg();
//        msg.setUsername(msgJson.getString("username"));
//        msg.setMsg(msgJson.getString("msg"));
        String[] msgs = msgInfo.split(":");
        msg.setUsername(msgs[0]);
        msg.setMsg(msgs[1]);
        msg.setTime(date);
        int i = msgDao.insertSingle(msg);
        //插入数据库成功
        if(i == 1){
            int mid = msg.getMid();
            ValueOperations valueOperations = redisTemplate.opsForValue();
//            int last_mid = valueOperations.get("");
        }
        //将收到的mid写入redis


    }

    /**
     * 异步插入用户信息
     */
    @Async("asyncServiceExecutor")
    public void insertUser(User user){
        userDao.insertUser(user);
    }

    /**
     * 异步修改用户下线时间
     */
    @Async("asyncServiceExecutor")
    public void updateOfflineTimeUser(User user){
        userDao.updateOfflineTimeUser(user);
    }

    /**
     * 异步修改用户上线时间
     */
    @Async("asyncServiceExecutor")
    public void updateOnlineTimeUser(String userid,Date time){
        User user = userDao.findUserByUsername(userid);
        user.setLast_online_time(time);
        int i = userDao.updateOnlineTimeUser(user);
        if(i != 0){
            logger.error("修改用户名为（"+userid+"）的用户信息出现错误");
        }
    }

}
