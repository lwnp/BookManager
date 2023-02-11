package com.xzit.bookmanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xzit.bookmanager.dao.mapper.BookMapper;
import com.xzit.bookmanager.dao.mapper.BorrowInfoMapper;
import com.xzit.bookmanager.dao.mapper.BorrowMapper;
import com.xzit.bookmanager.dao.mapper.HistoryMapper;
import com.xzit.bookmanager.entity.Book;
import com.xzit.bookmanager.entity.History;
import com.xzit.bookmanager.entity.OriginBorrow;
import com.xzit.bookmanager.service.BookService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;
    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    Gson gson;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    BorrowInfoMapper borrowInfoMapper;
    History history=new History();


    @Override
    public PageInfo<Book> getBookList(int pageNum,int rows) {
        PageHelper.startPage(pageNum,rows);
        List<Book> booklist=bookMapper.selectList(null);
        return new PageInfo<>(booklist);
    }

    @Override
    public Integer addBook(Book book) {
        ValueOperations<String,String> ops=redisTemplate.opsForValue();
        ops.set(book.getISBN(),gson.toJson(book));
        return bookMapper.insert(book);
    }

    @Override
    public Boolean isExist(Book book) {
        ValueOperations<String,String> ops=redisTemplate.opsForValue();
        if(Boolean.TRUE.equals(ops.getOperations().hasKey(book.getISBN()))){
            return true;
        }
        else {
            List<Book> booklist=bookMapper.selectList(null);
            for (Book bk : booklist) {
                if(bk.getISBN().equals(book.getISBN())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Integer deleteBookByIsbn(String ISBN) {
        redisTemplate.delete(ISBN);
        return bookMapper.delByISBN(ISBN);
    }

    @Override
    public Integer updateBook(Book book) {
        ValueOperations<String,String> ops=redisTemplate.opsForValue();
        ops.getAndSet(book.getISBN(), gson.toJson(book));
        return bookMapper.updateBook(book);
    }

    @Override
    public PageInfo<Book> getAvailableBook(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> bookList =bookMapper.selectList(null);
        return new PageInfo<>(bookList);
    }

    @Override
    public Integer returnBook(String username) {
        history.setUsername(username);
        List<OriginBorrow> borrowList=borrowMapper.getOriginBorrows(username);
        for(OriginBorrow borrow : borrowList){
            Book book = gson.fromJson(borrow.getBook(), Book.class);
            book.setNumber(book.getNumber()+1);
            bookMapper.updateBook(book);
            history.setISBN(book.getISBN());
            history.setRdate(new Date());
            history.setBdate(borrow.getDate());
            historyMapper.insert(history);

        }
        borrowMapper.returnBorrow(username);
        return borrowInfoMapper.returnBook(username);

    }

    @Override
    public PageInfo<Book> searchBook(String query,int pageNum,int pageSize) {
        QueryWrapper<Book> wrapper=new QueryWrapper<>();
        wrapper.like("title",query);
        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList=bookMapper.selectList(wrapper);
        return new PageInfo<>(bookList);
    }
}
