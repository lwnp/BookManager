package com.xzit.bookmanager.service;

import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.entity.AuthUser;

import javax.servlet.http.HttpSession;

public interface UserService {
    public void register(AuthUser authUser);
    public AuthUser findUser(HttpSession session);
    public PageInfo<AuthUser> getUserList(int pageNum,int pageSize);
    public Integer resetPassword(AuthUser authUser);
    public PageInfo<AuthUser> searchUserByQuery(String query,int pageNum,int pageSize);
}
