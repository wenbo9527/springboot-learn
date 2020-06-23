package com.example.springbootwebsocketredis.dao;

import com.example.springbootwebsocketredis.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgDao {
    /**
     * 获取所有消息
     * @return
     */
    public List<Msg> findMsgAll();

    /**
     * 插入单条数据
     * @param msg
     * @return
     */
    public int insertSingle(@Param("msg")Msg msg);

    /**
     * 批量插入数据
     */
    public int insertMsgMultiple(@Param("msgList") List<Msg> msgs);

    /**
     * 搜索离线消息
     */
    public List<Msg> findOfflineMsgByReceiveName(@Param("receiveName") String receiveName);

    /**
     * 修改消息接收状态
     */
    public int updateReceiveStatusByMid(@Param("mids") List<String> mids);

}
