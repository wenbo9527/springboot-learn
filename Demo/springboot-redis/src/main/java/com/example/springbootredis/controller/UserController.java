package com.example.springbootredis.controller;

import com.example.springbootredis.service.UserService;
import com.example.springbootredis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/findUserById")
    public Map<String,Object> findUserById(@RequestParam int id){
        User user = userService.findUserById(id);
        Map<String,Object> result = new HashMap<>();
        result.put("uid", user.getUid());
        result.put("uname",user.getUsername());
        result.put("pass",user.getPassword());
        result.put("salary",user.getSalary());
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteUserById")
    public int deleteUserById(@RequestParam int id){
        return userService.deleteUserById(id);
    }

}
