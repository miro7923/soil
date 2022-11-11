package com.august.soil.api.security;

import com.august.soil.api.model.user.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * HTTP Request-Header 에서 JWT 값을 추출하고, JWT 값이 올바르다면 인증정보 {@link JwtAuthenticationToken}를 생성한다.
 * 생성된 {@link JwtAuthenticationToken} 인스턴스는 {@link SecurityContextHolder}를 통해 Thread-Local 영역에 저장된다.
 * (Thread-Local 이라는 저장 영역을 잘 모르겠다면 구글 검색 Go~ 매우 중요한 부분입니다.)
 *
 * 인증이 완료된  {@link JwtAuthenticationToken#principal}부분 에는 {@link JwtAuthentication} 인스턴스가 set 된다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends GenericFilterBean {
  
  private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
  
  private final String headerKey;
  
  private final Jwt jwt;
  
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
  
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      String authorizationToken = obtainAuthorizationToken(request);
      log.info("@@@@@ authorizationToken: {}", authorizationToken);
      if (authorizationToken != null) {
        try {
          Jwt.Claims claims = verify(authorizationToken);
          log.info("Jwt parse result : {}", claims);
  
          if (canRefresh(claims, 6000 * 10)) {
            String refreshedToken = jwt.refreshToken(authorizationToken);
            response.setHeader(headerKey, refreshedToken);
          }
          
          Long userKey = claims.userKey;
          String name = claims.name;
          Email email = claims.email;
  
          List<GrantedAuthority> authorities = obtainAuthorities(claims);
          log.debug("@@@@@ authorities: {}", authorities.toString());
  
          if (nonNull(userKey) && isNotEmpty(name) && nonNull(email) && authorities.size() > 0) {
            log.debug("if (nonNull(userKey) && isNotEmpty(name) && nonNull(email) && authorities.size() > 0)");
            JwtAuthenticationToken authentication =
              new JwtAuthenticationToken(new JwtAuthentication(userKey, name, email), null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        } catch (Exception e) {
          log.warn("Jwt processing failed : {}", e.getMessage());
        }
      }
    } else {
      log.debug("SecurityContextHolder not populated with security token, as it already contained : {}",
        SecurityContextHolder.getContext().getAuthentication());
    }
    
    chain.doFilter(request, response);
  }
  
  private List<GrantedAuthority> obtainAuthorities(Jwt.Claims claims) {
    String[] roles = claims.roles;
    return roles == null || roles.length == 0 ?
      Collections.emptyList() :
      Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }
  
  private boolean canRefresh(Jwt.Claims claims, long refreshRangeMillis) {
    long exp = claims.exp();
    if (exp > 0) {
      long remain = exp - System.currentTimeMillis();
      return remain < refreshRangeMillis;
    }
    return false;
  }
  
  private Jwt.Claims verify(String token) {
    return jwt.verify(token);
  }
  
  private String obtainAuthorizationToken(HttpServletRequest request) {
    String token = request.getHeader(headerKey);
    if (token != null) {
      if (log.isDebugEnabled())
        log.debug("Jwt authorization api detected : {}", token);
      try {
        token = URLDecoder.decode(token, "UTF-8");
        String[] parts = token.split(" ");
        if (parts.length == 2) {
          String scheme = parts[0];
          String credentials = parts[1];
          return BEARER.matcher(scheme).matches() ? credentials : null;
        }
      } catch (UnsupportedEncodingException e) {
        log.error(e.getMessage(), e);
      }
    }
  
    return null;
  }
}
