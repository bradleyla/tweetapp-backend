package com.cogent.replyservice.exception;

import lombok.Data;

@Data
public class CustomRuntimeException extends RuntimeException{
    public String errorCode;
    public int status;

    public CustomRuntimeException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
