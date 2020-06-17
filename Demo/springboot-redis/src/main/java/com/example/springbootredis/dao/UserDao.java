package com.example.springbootredis.dao;

import com.example.springbootredis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    List<User> queryAll();

    User findUserById(int id);

    int updateUser(@Param("user") User user);

    int deleteUserById(int id);

}
