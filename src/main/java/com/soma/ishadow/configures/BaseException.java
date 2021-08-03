package com.soma.ishadow.configures;

public class BaseException extends Exception{

    private final BaseResponseStatus status;

    public BaseResponseStatus getStatus() {
        return status;
    }

    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }
}
