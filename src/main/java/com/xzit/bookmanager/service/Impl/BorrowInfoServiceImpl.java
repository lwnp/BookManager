package com.xzit.bookmanager.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xzit.bookmanager.dao.mapper.BorrowInfoMapper;
import com.xzit.bookmanager.entity.BorrowInfo;
import com.xzit.bookmanager.service.BorrowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowInfoServiceImpl implements BorrowInfoService {
    @Autowired
    BorrowInfoMapper borrowInfoMapper;
    @Override
    public PageInfo<BorrowInfo> getBorrowInfo(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<BorrowInfo> list=borrowInfoMapper.getInfoList();
        return new PageInfo<>(list);
    }
}
