package com.august.soil.api.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.builder.ToStringBuilder.*;

/**
 * 인증을 요청할 때 사용하는 DTO
 */
@Getter
@AllArgsConstructor
public class AuthenticationRequest {
  
  private String principal;
  
  private String credentials;
  
  protected AuthenticationRequest() {}
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
