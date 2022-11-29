package com.august.soil.api.security;

import com.august.soil.api.error.NotFoundException;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.Role;
import com.august.soil.api.model.user.User;
import com.august.soil.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * {@link org.springframework.security.authentication.ProviderManager#providers} 목록에 포함되 있다.
 * UserService를 통해 사용자 정보를 데이터베이스에서 조회 후 실질적인 사용자 인증 처리 로직 수행 및 JWT 생성
 * 인증 결과는 JwtAuthenticationToken 타입으로 반환한다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
  
  private final Jwt jwt;
  
  private final UserService userService;
  
  /**
   * {@link org.springframework.security.authentication.ProviderManager#authenticate} 메소드에서 호출된다.
   *
   * true 를 리턴하면 현재 Provider 에서 인증 처리를 할 수 있음을 의미한다.
   * 본 Provider 에서는 {@link JwtAuthenticationToken} 타입의 {@link Authentication} 를 처리할 수 있다.
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return ClassUtils.isAssignable(JwtAuthenticationToken.class, authentication);
  }
  
  /**
   * {@link org.springframework.security.authentication.ProviderManager#authenticate} 메소드에서 호출된다.
   *
   * null 이 아닌 값을 반환하면 인증 처리가 완료된다.
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
    return processUserAuthentication(authenticationToken.authenticationRequest());
  }
  
  private Authentication processUserAuthentication(AuthenticationRequest request) {
    try {
      User user = userService.login(new Email(request.getPrincipal()), request.getCredentials());
      JwtAuthenticationToken authenticated =
        // 응답용 Authentication 인스턴스를 생성한다.
        // JwtAuthenticationToken.principal 부분에는 JwtAuthentication 인스턴스가 set 된다.
        // 로그인 완료 전 JwtAuthenticationToken.principal 부분은 Email 인스턴스가 set 되어 있었다.
        new JwtAuthenticationToken(new JwtAuthentication(user.getId(), user.getName(), user.getEmail()), null, AuthorityUtils.createAuthorityList(Role.USER.getValue()));
      // JWT 값을 생성한다.
      // 권한은 ROLE_USER 를 부여한다.
      String apiToken = user.newApiToken(jwt, new String[]{Role.USER.getValue()});
      authenticated.setDetails(new AuthenticationResult(apiToken, user));
      return authenticated;
    } catch (NotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException(e.getMessage());
    } catch (DataAccessException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }
}
