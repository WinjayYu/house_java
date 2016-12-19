package com.ryel.zaja.config.bean;


import java.io.Serializable;

/**
 * Created by wangbin on 14-10-17.
 */
public class Result implements Serializable {

    private int status;

    private String msg ="";

    private Object data = "";

    public Result(int status) {
        this.status = status;
    }

    public Result() {
    }

    public Result msg(String msg) {
        this.msg = msg;
        return this;
    }


    public Result data(Object data) {
        this.data = data;
        return this;
    }

    public static Result success() {
        Result result = new Result();
        result.setStatus(ResultStatus.Success);
        result.setMsg("");
        return result;
    }

    public static Result success(String info) {
        Result result = new Result();
        result.setMsg(info);
        result.setStatus(ResultStatus.Success);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setStatus(ResultStatus.Error);
        return result;
    }

    public static Result noLogin() {
        Result result = new Result();
        result.setStatus(ResultStatus.Not_Login);
        return result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
