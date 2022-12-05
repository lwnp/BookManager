package com.xzit.bookmanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xzit.bookmanager.dao.mapper.BookMapper;
import com.xzit.bookmanager.dao.mapper.BorrowInfoMapper;
import com.xzit.bookmanager.dao.mapper.BorrowMapper;
import com.xzit.bookmanager.entity.*;
import com.xzit.bookmanager.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    Gson gson;
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    BorrowInfoMapper borrowInfoMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public Integer borrowBook(AuthUser user, Book book) {
        ValueOperations<String,String> ops=redisTemplate.opsForValue();
        book.setNumber(book.getNumber()-1);
        bookMapper.updateBook(book);
        ops.getAndSet(book.getISBN(),gson.toJson(book));
        BorrowInfo info=new BorrowInfo();
        info.setUsername(user.getUsername());
        Integer result=borrowMapper.insertBorrow(user.getUsername(), gson.toJson(book));
        info.setBooknum(borrowMapper.selectBorrowNumber(user.getUsername()));
        borrowInfoMapper.updateBorrowInfo(info);
        return result;
    }

    @Override
    public List<Borrow> getBookList(AuthUser user) {
        List<OriginBorrow> borrowList=borrowMapper.getOriginBorrows(user.getUsername());
        List<Borrow> list=new ArrayList<>();
        for (OriginBorrow originBorrow : borrowList) {
            Borrow borrow=new Borrow();
            borrow.setBook(gson.fromJson(originBorrow.getBook(),Book.class));
            borrow.setDate(originBorrow.getDate());
            borrow.setUsername(borrow.getUsername());
            list.add(borrow);
        }
        return list;
    }
}
