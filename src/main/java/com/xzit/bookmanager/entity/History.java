package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_history")
public class History {
    @TableField("username")
    String username;
    @TableField("ISBN")
    String ISBN;
    @TableField("bdate")
    Date bdate;
    @TableField("rdate")
    Date rdate;
}
