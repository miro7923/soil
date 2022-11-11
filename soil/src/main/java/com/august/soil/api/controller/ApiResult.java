package com.august.soil.api.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@RequiredArgsConstructor
@Getter
public class ApiResult<T> {
  
  @ApiModelProperty(value = "API 요청 처리 결과", required = true)
  private final boolean success;
  
  @ApiModelProperty(value = "success가 true라면, API 요청에 대한 응답값이 세팅됨(generic)")
  private final T response;
  
  @ApiModelProperty(value = "success가 false라면, API 요청에 대한 실패 이유")
  private final ApiError error;
  
  public static <T> ApiResult<T> OK(T response) {
    return new ApiResult<>(true, response, null);
  }
  
  public static ApiResult<?> ERROR(Throwable throwable, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(throwable, status));
  }
  
  public static ApiResult<?> ERROR(String errorMessage, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(errorMessage, status));
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
