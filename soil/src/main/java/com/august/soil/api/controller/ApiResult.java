package com.august.soil.api.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * API 응답 결과를 세팅하기 위한 클래스
 * 응답 결과를 통일하면서 API에 따라 다르게 리턴되어야 하는 데이터 처리를 위해 제네릭을 사용하였다.
 * @param <T> 응답으로 리턴할 객체
 */
@RequiredArgsConstructor
@Getter
public class ApiResult<T> {
  
  @ApiModelProperty(value = "API 요청 처리 결과", required = true)
  private final boolean success;
  
  @ApiModelProperty(value = "success가 true라면, API 요청에 대한 응답값이 세팅됨(generic)")
  private final T response;
  
  @ApiModelProperty(value = "success가 false라면, API 요청에 대한 실패 이유")
  private final ApiError error;
  
  /**
   * 성공 응답을 보낼 때 사용하면 된다. static 메서드이기 때문에 객체 생성 없이 호출할 수 있다.
   * 만약 리턴할 객체가 없고 단순 메시지만 리턴할 것이라면 ApiResult.OK("message");와 같은 형태로 호출하면 된다.
   * @param response 응답 결과로 리턴할 객체
   * @return 성공 응답과 그 데이터
   * @param <T> 참고할 클래스 타입
   */
  public static <T> ApiResult<T> OK(T response) {
    return new ApiResult<>(true, response, null);
  }
  
  /**
   * 실패 응답을 보낼 때 사용하면 된다. static 메서드이기 때문에 객체 생성 없이 호출할 수 있다.
   * @param throwable 던질 예외
   * @param status HTTP 상태 코드
   * @return 실패 응답
   */
  public static ApiResult<?> ERROR(Throwable throwable, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(throwable, status));
  }
  
  /**
   * 실패 응답을 보낼 때 사용하면 된다. static 메서드이기 때문에 객체 생성 없이 호출할 수 있다.
   * @param errorMessage 실패 메세지
   * @param status HTTP 상태 코드
   * @return 실패 응답
   */
  public static ApiResult<?> ERROR(String errorMessage, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(errorMessage, status));
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
