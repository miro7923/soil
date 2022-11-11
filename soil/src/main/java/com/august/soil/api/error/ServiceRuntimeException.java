package com.august.soil.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class ServiceRuntimeException extends RuntimeException {
  
  private final String messageKey;
  
  private final String detailKey;
  
  private final Object[] params;
}
