package com.xzit.bookmanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.dao.mapper.HistoryMapper;
import com.xzit.bookmanager.entity.History;
import com.xzit.bookmanager.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Resource
    HistoryMapper historyMapper;
    @Override
    public PageInfo<History> getHistoryList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<History> historyList=historyMapper.selectList(null);
        return new PageInfo<>(historyList);
    }

    @Override
    public PageInfo<History> getHistoryListByQuery(String query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<History> historyList=historyMapper.getHistoryByQuery(query);
        return new PageInfo<>(historyList);
    }
}
