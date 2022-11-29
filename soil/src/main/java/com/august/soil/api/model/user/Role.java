package com.august.soil.api.model.user;

import lombok.Getter;

/**
 * 인증된 사용자에게 인가를 위한 역할을 부여할 때 사용
 */
@Getter
public enum Role {
  
  USER("ROLE_USER");
  
  private final String value;
  
  Role(String value) {
    this.value = value;
  }
  
  public static Role of(String name) {
    for (Role role : Role.values()) {
      if (role.name().equalsIgnoreCase(name)) {
        return role;
      }
    }
    return null;
  }
}
