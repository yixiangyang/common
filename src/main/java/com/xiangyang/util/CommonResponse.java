package com.xiangyang.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> {
    private Integer status = 200;
    private String code = "SUCCESS";
    private String msg = "成功";
    private T data;

    public static <T> CommonResponse<T> fail(String code, String msg) {
        CommonResponse<T> result = new CommonResponse<>();
        result.status = 500;
        result.code = code;
        result.msg = msg;
        return result;
    }
}