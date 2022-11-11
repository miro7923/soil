package com.august.soil.api.model.category;

import com.august.soil.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.FetchType.*;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
public class Category {

  @Id @GeneratedValue
  @Column(name = "category_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private final User user;

  @Column(nullable = false)
  private final String name;
  
  public Category() {
    this.id = null;
    this.user = null;
    this.name = null;
  }

  public Category(User user, String name) {
      this(null, user, name);
  }

  public Category(Long id, User user, String name) {
    checkArgument(user != null, "user must be provided.");
    checkArgument(name != null, "name must be provided.");
    checkArgument(
      name.length() >= 1 && name.length() <= 10,
      "name length must be between 1 and 10 characters."
    );

    this.id = id;
    this.user = user;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Category category = (Category) o;
    return Objects.equals(id, category.id) && Objects.equals(user, category.user) &&
      Objects.equals(name, category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, name);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

  @AllArgsConstructor
  @NoArgsConstructor
  static public class Builder {
    private Long id;
    private User user;
    private String name;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Category build() {
      return new Category(id, user, name);
    }
  }
}
