package com.august.soil.api.model.category;

import com.august.soil.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.persistence.FetchType.LAZY;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * 모든 model 클래스는 JPA 영속성 컨텍스트를 통해 관리하기 때문에 이에 대한 이해가 없으면 코드를 보기 어려울 수 있다.
 */
@Entity
@Getter
@Builder
public class Category implements Serializable {

  @Id @GeneratedValue
  @Column(name = "category_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private final User user;

  @Column(nullable = false)
  private String name;
  
  public Category() {
    this.id = null;
    this.user = null;
    this.name = null;
  }

  /**
   * 생성자 단계에서 유효성 검증 후 객체가 생성될 수 있도록 함
   * @param name 새로 생성될 카테고리의 이름
   */
  public Category(Long id, User user, String name) {
    checkArgument(name != null, "name must be provided.");
    checkArgument(
      name.length() >= 1 && name.length() <= 10,
      "name length must be between 1 and 10 characters."
    );

    this.id = id;
    this.user = user;
    this.name = name;

  }
  public Category(User user, String name) {
    this(null, user, name);
  }

  /**
   * 개별 수정이 필요한 필드에 setter 생성
   */
  public void setName(String name) { this.name = name; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Category category = (Category) o;
    return Objects.equals(id, category.id)&&Objects.equals(name, category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
