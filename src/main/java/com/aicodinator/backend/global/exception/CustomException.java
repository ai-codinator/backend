package com.aicodinator.backend.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;

  public CustomException(ErrorCode errorCode){
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = null;
  }

  public CustomException(ErrorCode errorCode, String message) {
      super(message);
      this.errorCode = errorCode;
      this.message = message;
  }
}
