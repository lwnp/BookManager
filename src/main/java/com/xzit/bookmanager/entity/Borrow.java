package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_borrow")
public class Borrow {
    String username;
    Book book;
    Date date;

}
