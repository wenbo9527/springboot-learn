package com.example.springbootwebsocketredis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootWebsocketRedisApplicationTests {

//    @Autowired
//    public RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        hashOperations.delete("user","周文博");
//        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("111","nihao");
//        redisTemplate.convertAndSend("Mylistener1","Redis发布的第一条消息");
    }

}
