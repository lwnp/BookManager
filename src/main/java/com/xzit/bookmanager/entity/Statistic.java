package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Statistic {
    int booknum;
    int usernum;
    int borrownum;
}
