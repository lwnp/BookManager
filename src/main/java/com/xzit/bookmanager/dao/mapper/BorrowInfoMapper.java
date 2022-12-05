package com.xzit.bookmanager.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.entity.BorrowInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {
    @Update("update t_borrowinfo set booknum=#{booknum} where username=#{username}")
    Integer updateBorrowInfo(BorrowInfo borrowInfo);
    @Insert("insert into t_borrowinfo (username, booknum) VALUES (#{user.username},0)")
    Integer initBorrowInfo(@Param("user") AuthUser user);
    @Select("select * from t_borrowinfo where booknum>0")
    List<BorrowInfo> getInfoList();
    @Update("update t_borrowinfo set booknum=0 where username=#{username}")
    Integer returnBook(String username);
}
