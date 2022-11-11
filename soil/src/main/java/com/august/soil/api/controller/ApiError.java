package com.august.soil.api.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Getter
public class ApiError {
  
  @ApiModelProperty(value = "오류 메시지", required = true)
  private final String message;
  
  @ApiModelProperty(value = "HTTP 오류 코드", required = true)
  private final int status;
  
  public ApiError(Throwable throwable, HttpStatus status) {
    this(throwable.getMessage(), status);
  }
  
  public ApiError(String message, HttpStatus status) {
    this.message = message;
    this.status = status.value();
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
