package com.xzit.bookmanager.service.Impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    Gson gson;
    @Override
    public UserDetails loadUserByUsername(String username) {
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        String json=ops.get(username);
        AuthUser user=gson.fromJson(json, AuthUser.class);
        if (user == null) {
            user=userMapper.getUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("账号或密码错误！");
            }
            else {
                ops.set(user.getUsername(),gson.toJson(user));
                return User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();

            }

        }

        else {
            return User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        }
    }
}
