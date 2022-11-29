package com.august.soil.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예외처리를 Customizing 할 때 사용할 추상클래스
 * 이 클래스를 상속받아서 Custom 예외 클래스를 만들면 된다.
 */
@RequiredArgsConstructor
@Getter
public abstract class ServiceRuntimeException extends RuntimeException {
  
  private final String messageKey;
  
  private final String detailKey;
  
  private final Object[] params;
}
