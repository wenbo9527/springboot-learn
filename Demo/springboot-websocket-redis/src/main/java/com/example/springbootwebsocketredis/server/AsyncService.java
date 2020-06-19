package com.example.springbootwebsocketredis.server;

import com.example.springbootwebsocketredis.dao.MsgDao;
import com.example.springbootwebsocketredis.entity.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncService{

    private static Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private MsgDao msgDao;

    @Async("asyncServiceExecutor")
    public void insertIntoMysql(String msgInfo){
        logger.info("线程-" + Thread.currentThread().getId() + "正在执行插入语句");
        Msg msg = new Msg();
        String[] msgInfos = msgInfo.split(":");
        msg.setMsg(msgInfos[1]);
        msg.setUsername(msgInfos[0]);
        msgDao.insertSingle(msg);
    }
}
