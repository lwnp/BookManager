package com.xzit.bookmanager.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzit.bookmanager.entity.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HistoryMapper extends BaseMapper<History> {
    @Select("select * from t_history where username like concat('%',#{query},'%') or ISBN like concat('%',#{query},'%')")
    List<History> getHistoryByQuery(String query);

}
