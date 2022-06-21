package com.aiit.kafkaclient.exception;

import com.aiit.kafkaclient.enums.ResultCodeEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private String errorCode;

    private String errorMsg;

    public ApiException(ResultCodeEnum resultCodeEnum) {
        this.errorCode = resultCodeEnum.getCode();
        this.errorMsg = resultCodeEnum.getMessage();
    }

    public ApiException(String message) {
        super(message);
    }
}
