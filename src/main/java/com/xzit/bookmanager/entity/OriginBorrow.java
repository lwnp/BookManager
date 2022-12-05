package com.xzit.bookmanager.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OriginBorrow {
    String username;
    String book;
    Date  date;
}
