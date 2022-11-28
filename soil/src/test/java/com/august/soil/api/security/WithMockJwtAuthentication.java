package com.august.soil.api.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {
  
  long id() default 2L;
  
  String name() default "tester00";
  
  String email() default "tester00@gmail.com";
  
  String role() default "ROLE_USER";
}
