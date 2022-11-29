package com.august.soil.api.security;

import com.august.soil.api.model.user.User;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * 인증 요청에 따른 응답을 반환할 때 사용할 DTO
 */
@Getter
public class AuthenticationResult {
  
  private final String apiToken;
  
  private final User user;
  
  public AuthenticationResult(String apiToken, User user) {
    checkArgument(apiToken != null, "apiToken must be provided.");
    checkArgument(user != null, "user must be provided.");
    
    this.apiToken = apiToken;
    this.user = user;
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
