package com.xzit.bookmanager.service.Impl;

import com.google.gson.Gson;
import com.xzit.bookmanager.dao.mapper.BookMapper;
import com.xzit.bookmanager.dao.mapper.BorrowMapper;
import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.Statistic;
import com.xzit.bookmanager.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    Gson gson;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Statistic getStatisticNum() {
        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        return gson.fromJson(operations.get("statistic"), Statistic.class);
    }
}
