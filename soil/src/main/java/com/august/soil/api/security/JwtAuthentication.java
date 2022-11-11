package com.august.soil.api.security;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.User;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * 인증된 사용자를 표현한다.
 */
public class JwtAuthentication {
  
  public final Id<User, Long> id;
  
  public final String name;
  
  public final Email email;
  
  public JwtAuthentication(Long id, String name, Email email) {
    checkArgument(id != null, "id must be provided.");
    checkArgument(name != null, "name must be provided.");
    checkArgument(email != null, "email must be provided.");
    
    this.id = Id.of(User.class, id);
    this.name = name;
    this.email = email;
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
