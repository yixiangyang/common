package com.xiangyang.exception;

import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;

@Data
@ToString
public class CodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private long timestamp = System.currentTimeMillis();

    public CodeException() {
    }

    public CodeException(String code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }

    public CodeException(String code, String msg, long timestamp) {
        super(code + ":" + msg + ":" + timestamp);
        this.code = code;
        this.msg = msg;
        this.timestamp = timestamp;
    }

}
