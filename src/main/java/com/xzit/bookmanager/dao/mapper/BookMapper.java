package com.xzit.bookmanager.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzit.bookmanager.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface BookMapper extends BaseMapper<Book> {
    @Select("select sum(number) from t_book")
    Integer countBooks();
    @Insert("insert into t_book (ISBN, author, title, publisher, number, description)VALUES (#{ISBN},#{author},#{title},#{publisher},#{number},#{description}) ")
    Integer insertBook(Book book);
    @Delete("delete from t_book where ISBN =#{isbn}")
    Integer delByISBN(String isbn);
    @Update("update t_book set author=#{author},title=#{title},publisher=#{publisher},number=#{number},description=#{description} where ISBN=#{ISBN}")
    Integer updateBook(Book book);
    @Select("select * from t_book where number>0")
    List<Book> getAvailableBook();
}
