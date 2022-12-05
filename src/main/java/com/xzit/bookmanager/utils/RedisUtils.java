package com.xzit.bookmanager.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzit.bookmanager.dao.mapper.BookMapper;
import com.xzit.bookmanager.dao.mapper.BorrowMapper;
import com.xzit.bookmanager.dao.mapper.UserMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.entity.Book;
import com.xzit.bookmanager.entity.Borrow;
import com.xzit.bookmanager.entity.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RedisUtils {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    Gson gson;
    ValueOperations<String,String> ops;
    public void ReadUserToRedis(){
        ops=redisTemplate.opsForValue();
        List<AuthUser> userList=userMapper.selectList(null);
        for(AuthUser user : userList){
            if(Boolean.FALSE.equals(ops.getOperations().hasKey(user.getUsername()))){
                ops.set(user.getUsername(), gson.toJson(user));
            }
            else if(!Objects.equals(ops.get(user.getUsername()), gson.toJson(user))){
                ops.getAndSet(user.getUsername(), gson.toJson(user));
            }
        }

    }
    public void ReadBookToRedis(){
        ops=redisTemplate.opsForValue();
        List<Book> bookList=bookMapper.selectList(null);
        for(Book book : bookList){
            if(Boolean.FALSE.equals(ops.getOperations().hasKey(book.getISBN()))){
                ops.set(book.getISBN(),gson.toJson(book));
            }
            else if(!Objects.equals(ops.get(book.getISBN()),gson.toJson(book))){
                ops.getAndSet(book.getISBN(), gson.toJson(book));
            }
        }

    }
//    public void ReadBorrowToRedis(){
//        ops=redisTemplate.opsForValue();
//        List<Borrow> borrowList=borrowMapper.selectList(null);
//        for(Borrow borrow : borrowList){
//            if(!ops.getOperations().hasKey())
//        }
//    }
    public void ReadStatisticToRedis(){
        ops=redisTemplate.opsForValue();
        int borrownum=borrowMapper.countBorrows();
        int usernum=userMapper.countUser();
        int booknum=bookMapper.countBooks();
        Statistic sts = new Statistic();
        sts.setUsernum(usernum);
        sts.setBorrownum(borrownum);
        sts.setBooknum(booknum);
        ops.set("statistic",gson.toJson(sts));
    }

}
