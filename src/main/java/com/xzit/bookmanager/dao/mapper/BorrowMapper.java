package com.xzit.bookmanager.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzit.bookmanager.entity.Borrow;
import com.xzit.bookmanager.entity.OriginBorrow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {
    @Select("select count(*) from t_borrow")
    Integer countBorrows();
    @Select("select distinct username from t_borrow")
    List<String> getBorrowUser();
    @Insert("insert into t_borrow (username, book, date) VALUES (#{username},#{book},CURDATE())")
    Integer insertBorrow(String username, String book);
    @Select("select * from t_borrow where username=#{username}")
    List<OriginBorrow> getOriginBorrows(String username);
    @Select("select count(*) from t_borrow where username=#{username}")
    Integer selectBorrowNumber(String username);
    @Delete("delete from t_borrow where username=#{username}")
    Integer returnBorrow(String username);

}
