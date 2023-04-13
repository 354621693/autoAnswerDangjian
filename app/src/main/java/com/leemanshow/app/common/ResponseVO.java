package com.leemanshow.app.common;

import lombok.Data;

@Data
public class ResponseVO<T> {
    T data;
    String msg;
    int code;

    public static <T> ResponseVO<T> success(T data) {
        ResponseVO<T> result = new ResponseVO<>();
        result.setData(data);
        result.setMsg("success");
        result.setCode(0);
        return result;
    }

    public static <T> ResponseVO<T> success(T data, String msg) {
        ResponseVO<T> result = new ResponseVO<>();
        result.setData(data);
        result.setMsg(msg);
        result.setCode(0);
        return result;
    }
}
