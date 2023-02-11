package com.xzit.bookmanager.service;

import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.entity.History;

public interface HistoryService {
    PageInfo<History> getHistoryList(int pageNum,int pageSize);
    PageInfo<History> getHistoryListByQuery(String query,int pageNum,int pageSize);
}
