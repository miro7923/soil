package com.august.soil.api.error;

import com.august.soil.api.util.MessageUtils;

public class UnauthorizedException extends ServiceRuntimeException {
  
  public static final String MESSAGE_KEY = "error.auth";
  
  public static final String MESSAGE_DETAILS = "error.auth.details";
  
  public UnauthorizedException(String message) {
    super(MESSAGE_KEY, MESSAGE_DETAILS, new Object[]{message});
  }
  
  @Override
  public String getMessage() {
    return MessageUtils.getMessage(getDetailKey(), getParams());
  }
  
  @Override
  public String toString() {
    return MessageUtils.getMessage(getMessageKey());
  }
}
