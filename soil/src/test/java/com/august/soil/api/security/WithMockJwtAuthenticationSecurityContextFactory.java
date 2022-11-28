package com.august.soil.api.security;

import com.august.soil.api.model.user.Email;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class WithMockJwtAuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuthentication> {
  
  @Override
  public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    JwtAuthenticationToken authentication =
      new JwtAuthenticationToken(
        new JwtAuthentication(annotation.id(), annotation.name(), new Email(annotation.email())),
        null,
        createAuthorityList(annotation.role())
      );
    context.setAuthentication(authentication);
    return context;
  }
}
