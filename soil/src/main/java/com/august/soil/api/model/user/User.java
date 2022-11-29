package com.august.soil.api.model.user;

import com.august.soil.api.security.Jwt;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.time.LocalDateTime.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * 사용자 정보를 표현하는 클래스로 새 토큰의 발급을 처리할 수 있는 비즈니스 로직을 담고 있다.
 */
@Entity
@Getter
@Builder
public class User implements Serializable {

  @Id @GeneratedValue
  @Column(name = "user_id")
  private final Long id;

  @Column(name = "sns_type")
  private final SnsType snsType;

  @Column(name = "name")
  private String name;
  
  // @Embedded를 통해 Email 클래스를 값 타입으로 매핑한다.
  @Column(name = "email")
  @Embedded
  private final Email email;

  @Column(name = "create_at")
  private final LocalDateTime createAt;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public User() {
    id = null;
    snsType = null;
    email = null;
    createAt = null;
  }
  
  public User(SnsType snsType, String name, Email email) {
    this(null, snsType, name, email, null);
  }
  
  public User(Long id, SnsType snsType, String name, Email email, LocalDateTime createAt) {
    checkArgument(name != null, "name must be provided.");
    checkArgument(
    name.length() >= 1 && name.length() <= 10,
    "name length must be between 1 and 10 characters."
    );
    checkArgument(email != null, "email must be provided.");

    this.id = id;
    this.snsType = snsType;
    this.name = name;
    this.email = email;
    this.createAt = defaultIfNull(createAt, now());
  }
  
  public String newApiToken(Jwt jwt, String[] roles) {
    Jwt.Claims claims = Jwt.Claims.of(id, name, email, roles);
    return jwt.newToken(claims);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(name, user.name) &&
      Objects.equals(email, user.email) && Objects.equals(createAt, user.createAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, createAt);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
