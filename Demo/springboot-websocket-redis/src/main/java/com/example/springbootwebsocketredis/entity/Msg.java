package com.example.springbootwebsocketredis.entity;

import java.util.Date;

public class Msg {

    private int mid;

    private String username;

    private String msg;

    private Date time;

    public Msg(){

    }

    public Msg(String username,String msg,Date time){
        this.username = username;
        this.msg = msg;
        this.time = time;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
