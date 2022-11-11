package com.august.soil.api.security;

import com.august.soil.api.controller.user.UserRestController;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  
  /**
   * 인증 주체를 나타낸다. 타입이 Object 라는 것에 주목한다.
   * 이것은 로그인 전과 로그인 후 타입이 다르기 때문이다.
   * 로그인 전에는 String 타입이고, 로그인 후에는 {@link JwtAuthentication} 타입이다.
   * ({@link JwtAuthenticationTokenFilter#doFilter} 80라인)
   *
   * 컨트롤러에서 {@link org.springframework.security.core.annotation.AuthenticationPrincipal} 어노테이션을 사용하면 쉽게 접근할 수 있다.
   * 단, 인증이 정상적으로 완료된 경우에만 사용해야 한다는 것에 주의한다.
   * ({@link UserRestController#me} 참고)
   */
  private final Object principal;
  
  private String credentials;
  
  public JwtAuthenticationToken(Object principal, String credentials) {
    super(null);
    super.setAuthenticated(false);
    
    this.principal = principal;
    this.credentials = credentials;
  }
  
  JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    super.setAuthenticated(true);
  
    this.principal = principal;
    this.credentials = credentials;
  }
  
  AuthenticationRequest authenticationRequest() {
    return new AuthenticationRequest(String.valueOf(principal), credentials);
  }
  
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    if (isAuthenticated) {
      throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }
    super.setAuthenticated(false);
  }
  
  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
