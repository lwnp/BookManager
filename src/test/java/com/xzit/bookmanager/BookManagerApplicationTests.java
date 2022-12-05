package com.xzit.bookmanager;

import com.google.gson.Gson;
import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BookManagerApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    Gson gson;

    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }
    @Test
    void insertUserTest() {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        AuthUser user=new AuthUser();
        user.setUsername("helloword");
        user.setPassword("{bcrypt}"+encoder.encode("123456"));
        user.setRole("user");
        userMapper.insertUser(user);

    }
    @Test
    void redisTest(){
        redisUtils.ReadUserToRedis();
        ValueOperations<String,String> OPS= stringRedisTemplate.opsForValue();
        OPS.set("test","123456");

    }
    @Test
    void responseTest() {
        System.out.println();

    }

}
