package com.xzit.bookmanager.service;

import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.entity.Book;


public interface BookService {
    public PageInfo<Book> getBookList(int pageNum,int rows);
    Integer addBook(Book book);
    Boolean isExist(Book book);
    Integer deleteBookByIsbn(String ISBN);
    Integer updateBook(Book book);
    PageInfo<Book> getAvailableBook(int pageNum,int pageSize);
    Integer returnBook(String username);
    PageInfo<Book> searchBook(String query,int pageNum,int pageSize);
}
