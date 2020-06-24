package com.example.springbootwebsocketredis.dao;

import com.example.springbootwebsocketredis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    public List<User> findAllUser();

    public int insertUser(@Param("user") User user);

    public int updateOfflineTimeUser(@Param("user") User user);

    public int updateOnlineTimeUser(@Param("user") User user);

    public User findUserByUsername(@Param("username") String username);

}
