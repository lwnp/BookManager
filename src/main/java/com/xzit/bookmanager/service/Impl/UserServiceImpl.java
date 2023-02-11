package com.xzit.bookmanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xzit.bookmanager.dao.mapper.BorrowInfoMapper;
import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    @Autowired
    BorrowInfoMapper borrowInfoMapper;
    @Autowired
    Gson gson;

    @Override
    public void register(AuthUser authUser) {
       authUser.setPassword("{bcrypt}"+encoder.encode(authUser.getPassword()));
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(authUser.getUsername(),gson.toJson(authUser));
        borrowInfoMapper.initBorrowInfo(authUser);
       if (userMapper.insert(authUser)<=0) {
           throw new RuntimeException("Failed!");

        }

    }

    @Override
    public AuthUser findUser(HttpSession session) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        AuthUser user = (AuthUser) session.getAttribute("user");
        if(user == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user=gson.fromJson(operations.get(authentication.getName()), AuthUser.class);
            session.setAttribute("user", user);
        }
        return user;
    }

    @Override
    public PageInfo<AuthUser> getUserList(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<AuthUser> userList= userMapper.selectList(null);
        return new PageInfo<>(userList);
    }

    @Override
    public Integer resetPassword(AuthUser authUser) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.getAndSet(authUser.getUsername(),gson.toJson(authUser));
        return userMapper.updateUserPassword(authUser);
    }

    @Override
    public PageInfo<AuthUser> searchUserByQuery(String query, int pageNum, int pageSize) {
        QueryWrapper<AuthUser> wrapper=new QueryWrapper<>();
        wrapper.like("username",query);
        PageHelper.startPage(pageNum,pageSize);
        List<AuthUser> userList=userMapper.selectList(wrapper);
        return new PageInfo<>(userList);
    }


}
