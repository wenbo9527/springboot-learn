package com.example.springbootwebsocketredis.entity;

import java.util.Date;

public class User {

    //用户名
    private String username;

    //用户上次离线时间
    private Date last_offline_time;

    //用户最近一次上线时间
    private Date last_online_time;

    //用户最后接收到的消息id
    private int last_receive_msg_id;

    public int getLast_receive_msg_id() {
        return last_receive_msg_id;
    }

    public void setLast_receive_msg_id(int last_receive_msg_id) {
        this.last_receive_msg_id = last_receive_msg_id;
    }

    public Date getLast_online_time() {
        return last_online_time;
    }

    public void setLast_online_time(Date last_online_time) {
        this.last_online_time = last_online_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLast_offline_time() {
        return last_offline_time;
    }

    public void setLast_offline_time(Date last_offline_time) {
        this.last_offline_time = last_offline_time;
    }
}
