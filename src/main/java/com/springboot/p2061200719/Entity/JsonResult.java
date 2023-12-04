package com.springboot.p2061200719.Entity;

import lombok.Data;

@Data
public class JsonResult<T> {
    private T data;
    private String code;
    private String msg;

    public JsonResult(){
        this.code = "200";
        this.msg = "操作成功！";
    }

    public JsonResult(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(T data){
        this.data = data;
        this.code = "200";
        this.msg = "操作成功！";
    }

    public JsonResult(T data,String msg){
        this.data = data;
        this.code = "200";
        this.msg = msg;
    }
}
