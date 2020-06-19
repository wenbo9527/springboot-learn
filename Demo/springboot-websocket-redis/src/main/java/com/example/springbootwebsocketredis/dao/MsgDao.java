package com.example.springbootwebsocketredis.dao;

import com.example.springbootwebsocketredis.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgDao {

    public List<Msg> findMsgAll();

    public int insertSingle(@Param("msg")Msg msg);

}
