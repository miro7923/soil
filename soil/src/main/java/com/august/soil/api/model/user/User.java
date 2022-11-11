package com.august.soil.api.model.user;

import com.august.soil.api.security.Jwt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.time.LocalDateTime.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
public class User {

  @Id @GeneratedValue
  @Column(name = "user_id")
  private final Long id;

  @Column(name = "sns_type")
  private final SnsType snsType;

  @Column(name = "name")
  private String name;
  
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
  
  @AllArgsConstructor
  @NoArgsConstructor
  static public class Builder {
    private Long id;
    private SnsType snsType;
    private String name;
    private Email email;
    private LocalDateTime createAt;
    
    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder snsType(SnsType snsType) {
      this.snsType = snsType;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder email(Email email) {
      this.email = email;
      return this;
    }

    public Builder createAt(LocalDateTime createAt) {
      this.createAt = createAt;
      return this;
    }
    
    public User build() {
      return new User(id, snsType, name, email, createAt);
    }
  }
}
