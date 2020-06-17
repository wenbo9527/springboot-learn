package com.example.springbootwebsocketredis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootWebsocketRedisApplicationTests {

    @Test
    void contextLoads() {
//        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("111","nihao");
//        redisTemplate.convertAndSend("Mylistener1","Redis发布的第一条消息");
    }

}
