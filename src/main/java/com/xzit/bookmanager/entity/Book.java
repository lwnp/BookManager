package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_book")
public class Book {
    @TableId(value = "ISBN",type = IdType.NONE)
    String ISBN;
    String author;
    String title;
    String publisher;
    String description;
    int number;
}
