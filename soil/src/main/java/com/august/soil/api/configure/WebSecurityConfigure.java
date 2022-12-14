package com.august.soil.api.configure;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Role;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.*;
import com.august.soil.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring Security ??????????????? ?????? ????????????
 * SecurityFilterChain??? ?????? ?????? ????????? ????????? ????????? ??? ????????? ??????????????? ????????????.
 */
@Component
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfigure {

  private final Jwt jwt;
  
  private final JwtTokenConfigure jwtTokenConfigure;
  
  private final JwtAccessDeniedHandler accessDeniedHandler;
  
  private final EntryPointUnauthorizedHandler unauthorizedHandler;
  
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/swagger-resources", "/static/**", "/templates/**");
  }
  
  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserService userService) {
    return new JwtAuthenticationProvider(jwt, userService);
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public ConnectionBasedVoter connectionBasedVoter() {
    final String regex = "^api/diaries/(\\d+)/.*$";
    final Pattern pattern = Pattern.compile(regex);
    RequestMatcher requiresAuthorizationRequestMatcher = new RegexRequestMatcher(pattern.pattern(), null);
    return new ConnectionBasedVoter(
      requiresAuthorizationRequestMatcher,
      (String url) -> {
        // url?????? targetId??? ???????????? ?????? ????????? ??????
        Matcher matcher = pattern.matcher(url);
        long id = matcher.matches() ? NumberUtils.toLong(matcher.group(1), -1) : -1;
        log.info("@@@@@ id: {}", id);
        return Id.of(User.class, id);
      }
    );
  }
  
  @Bean
  public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
    decisionVoters.add(new WebExpressionVoter());
    // voter ????????? connectionBasedVoter ??? ?????????
    decisionVoters.add(connectionBasedVoter());
    // ?????? voter?????? ???????????? ???
    return new UnanimousBased(decisionVoters);
  }
  
  public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
    log.info("@@@@@ jwtTokenConfigure.getHeader(): {}", jwtTokenConfigure.getHeader());
    return new JwtAuthenticationTokenFilter(jwtTokenConfigure.getHeader(), jwt);
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, Jwt jwt) throws Exception {
    http
      .csrf()
        .disable()
      .headers()
        .disable()
      .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
      .sessionManagement()
        // JWT ????????? ??????????????? ?????????(STATELESS) ?????? ??????
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .authorizeRequests()
        .antMatchers("api/auth").permitAll()
//        .antMatchers("api/users/join").permitAll()
        .antMatchers("api/**").hasRole(Role.USER.name())
        .accessDecisionManager(accessDecisionManager())
        .anyRequest().permitAll()
        .and()
      .cors()
        .configurationSource(corsConfigurationSource())
        .and()
      // JWT ????????? ??????????????? form ???????????? ???????????????
      .formLogin()
        .disable()
      // ?????? ?????? ??????
      // UsernamePasswordAuthenticationFilter ?????? jwtAuthenticationTokenFilter ??? ????????????.
      .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    
    corsConfiguration.addAllowedOriginPattern("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true);
  
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
  
    return source;
  }
}
