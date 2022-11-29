package com.august.soil.api.security;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.User;
import com.august.soil.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.function.Function;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

/**
 * 인가(Authorization)를 요청받은 사용자에 대해서 적합한 권한을 가지고 있는지 확인 후 인가 허용을 결정하는 컴포넌트
 */
public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {
  
  private final RequestMatcher requiresAuthorizationRequestMatcher;
  
  private final Function<String, Id<User, Long>> idExtractor;
  
  private UserService userService;
  
  public ConnectionBasedVoter(RequestMatcher requiresAuthorizationRequestMatcher, Function<String, Id<User, Long>> idExtractor) {
    checkArgument(requiresAuthorizationRequestMatcher != null, "requiresAuthorizationRequestMatcher must be provided.");
    checkArgument(idExtractor != null, "idExtractor must be provided.");
    
    this.requiresAuthorizationRequestMatcher = requiresAuthorizationRequestMatcher;
    this.idExtractor = idExtractor;
  }
  
  /**
   * 주어진 정보를 바탕으로 이 사용자를 인가해도 되는지 확인한 후 접근 권한을 부여하거나 거절/보류한다.
   * @param authentication the caller making the invocation
   * @param fi the secured object being invoked
   * @param attributes the configuration attributes associated with the secured object
   * @return 접근 권한 부여/거절/보류
   */
  @Override
  public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
    HttpServletRequest request = fi.getRequest();
    
    // 인가 가능한 사용자면 접근 권한을 부여한다.
    if (!requiresAuthorization(request))
      return ACCESS_GRANTED;
    
    // JwtAuthenticationToken 생성이 불가능하다면 접근 거부한다.
    if (!isAssignable(JwtAuthenticationToken.class, authentication.getClass()))
      return ACCESS_ABSTAIN;
    
    JwtAuthentication jwtAuth = (JwtAuthentication) authentication.getPrincipal();
    // 요청받은 URI에서 ID를 얻는다.
    Id<User, Long> targetId = obtainTargetId(request);
    
    // jwt 인증정보와 비교해서 URI에서 얻은 ID가 본인이라면(현재 인증되어 토큰이 발급된 사용자) 접근 권한 부여
    if (jwtAuth.id.equals(targetId))
      return ACCESS_GRANTED;
    
    return ACCESS_DENIED;
  }
  
  private Id<User, Long> obtainTargetId(HttpServletRequest request) {
    return idExtractor.apply(request.getRequestURI());
  }
  
  private boolean requiresAuthorization(HttpServletRequest request) {
    return requiresAuthorizationRequestMatcher.matches(request);
  }
  
  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return isAssignable(FilterInvocation.class, clazz);
  }
  
  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
