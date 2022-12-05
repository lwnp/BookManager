package com.xzit.bookmanager.service;

import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.entity.BorrowInfo;

public interface BorrowInfoService {
    PageInfo<BorrowInfo> getBorrowInfo(int pageNum,int pageSize);

}
