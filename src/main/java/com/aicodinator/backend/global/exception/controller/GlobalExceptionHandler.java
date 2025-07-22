package com.aicodinator.backend.global.exception.controller;

import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import com.aicodinator.backend.global.exception.ErrorResponse;
import com.aicodinator.backend.global.exception.ValidErrorResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
  /**
   * Validation 예외 처리
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidErrorResponse> handleValidationException(MethodArgumentNotValidException e){
    log.error("ValidationException 발생: {}", e.getMessage(), e);

    Map<String, String> validation = new HashMap<>();
    for(FieldError fieldError : e.getFieldErrors()){
      validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ValidErrorResponse response = ValidErrorResponse.builder()
        .errorCode(HttpStatus.BAD_REQUEST.toString())
        .errorMessage("잘못된 요청입니다.")
        .validation(validation)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * 커스텀 (비지니스) 예외 처리
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
    log.error("Custom Exception 발생: {}", e.getMessage(), e);

    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(errorCode.getMessage())
        .build();

    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  /**
   * 그 외 서버 (500) 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e){
    log.error("Unhandled Exception 발생: {}", e.getMessage(), e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
