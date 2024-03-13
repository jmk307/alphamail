package com.osanvalley.moamail.global.error.exception;

import com.osanvalley.moamail.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
}
