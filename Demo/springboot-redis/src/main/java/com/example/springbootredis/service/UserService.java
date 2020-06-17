package com.example.springbootredis.service;

import com.example.springbootredis.dao.UserDao;
import com.example.springbootredis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<User> queryAll(){
        return userDao.queryAll();
    }

    /**
     * 获取用户的策略：先从缓存中获取用户，没有则去数据库表，在将数据写入缓存中
     */
    public User findUserById(int id){
        String key = "user_"+id;
        ValueOperations<String,User> operations = redisTemplate.opsForValue();

        boolean hasKey = redisTemplate.hasKey(key);

        if(hasKey){
            long start = System.currentTimeMillis();
            User user = operations.get(key);
            System.out.println("===============从缓存中获取数据==================");
            System.out.println(user.getUsername());
            System.out.println("================================================");
            long end = System.currentTimeMillis();
            System.out.println("查询redis花费的时间是：" + (end - start) + "ms");
            return user;
        }else{
            long start = System.currentTimeMillis();
            User user = userDao.findUserById(id);
            System.out.println("===============从数据库表中获取数据==================");
            System.out.println(user.getUsername());
            System.out.println("================================================");
            operations.set(key, user, 5, TimeUnit.HOURS);
            long end = System.currentTimeMillis();
            System.out.println("查询数据库表花费的时间是：" + (end - start) + "ms");
            return user;
        }
    }

    /**
     * 更新用户策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存
     */
    public int updateUser(User user) {
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        int result = userDao.updateUser(user);
        if(result != 0){
            String key = "user_"+user.getUid();
            boolean hasKey = redisTemplate.hasKey(key);
            if(hasKey){
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key=============》"+key);
            }
            //在将更新后的数据加入缓存
            User userNew = userDao.findUserById(user.getUid());
            if(ObjectUtils.isEmpty(userNew)){
                operations.set(key, userNew, 5, TimeUnit.HOURS);
            }
        }
        return result;
    }

    /**
     * 删除用户策略：删除数据库表中数据，然后删除缓存
     */
    public int deleteUserById(int id){
        //int result = userDao.deleteUserById(id);
        int result = 1;
        if(result != 0){
            String key = "user_"+id;
            boolean hasKey = redisTemplate.hasKey(key);
            if(hasKey){
                redisTemplate.delete(key);
                System.out.println("删除了缓存中的key："+key);
            }
        }
        return result;
    }

}
