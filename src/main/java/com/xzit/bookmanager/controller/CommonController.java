package com.xzit.bookmanager.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzit.bookmanager.ResponseData.Response;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.service.UserService;
import com.xzit.bookmanager.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Controller
public class CommonController {
    @Resource
    RedisUtils redisUtils;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserService userService;
    @Autowired
    Gson gson;

    @PostConstruct
    public void init(){
        redisUtils.ReadUserToRedis();
        redisUtils.ReadBookToRedis();

    }


    @GetMapping("/login")
    String login(){
        return "login";

    }
    @GetMapping("/register")
    String register(){
        return "register";
    }

    @PostMapping(value = "/doregister")
    @ResponseBody
    public Response doRegister(@RequestParam("user") String username,@RequestParam("pwd") String password){
        ValueOperations<String, String> ops=stringRedisTemplate.opsForValue();
        if (username.isBlank() || password.isBlank()) {
            return Response.fail("账号或密码为空");

        }
        if(Boolean.TRUE.equals(ops.getOperations().hasKey(username))){
            return Response.fail("账号已存在");
        }
        else {
            AuthUser user = new AuthUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("user");
            userService.register(user);
            return Response.ok("注册成功");
        }
    }


}
