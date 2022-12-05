package com.xzit.bookmanager.ResponseData;

public class Response {
    public String msg;
    public boolean success;
    private Response(boolean success,String msg) {
        this.success = success;
        this.msg=msg;


    }
    public static Response ok(String msg){
        return new Response(true,msg);
    }
    public static Response fail(String msg){
        return new Response(false,msg);
    }
}
