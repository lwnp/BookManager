package com.xzit.bookmanager.service;

import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.entity.Book;
import com.xzit.bookmanager.entity.Borrow;

import java.util.List;

public interface BorrowService {
    Integer borrowBook(AuthUser user, Book book);
    List<Borrow> getBookList(AuthUser user);
}
