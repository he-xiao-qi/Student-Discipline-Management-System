package com.example.sdms.common;

public class ResultVO<T> {
    private int code;
    private String msg;
    private T data;

    public ResultVO() {}
    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(200, "success", data);
    }
    public static <T> ResultVO<T> error(String msg) {
        return new ResultVO<>(500, msg, null);
    }
    // getter/setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
} 