package com.august.soil.api.model.diary;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
@Builder
public class Diary implements Serializable {

  @Id @GeneratedValue
  @Column(name = "diary_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private final User user;
  
  /**
   * 정석적인 방법대로면 지연로딩방식(fetch = LAZY)이어야 하나 프록시 데이터의 JSON 데이터 직렬화 문제를 해결하지 못해 일단 즉시로딩으로 둠
   * TODO : 지연로딩방식으로 변경 가능한 방법을 찾으면 수정할 것
   */
  @ManyToOne(fetch = EAGER)
  @JoinColumn(name = "category_id")
  @JsonIgnore
  private Category category;

  @Column(nullable = false)
  private String title;
  private String content;
  private final LocalDateTime createAt;
  private String photoUrl;
  private String price;
  
  public Diary() {
    this.id = null;
    this.user = null;
    this.createAt = null;
  }

  public Diary(User user, Category category, String title) {
    this(null, user, category, title, null, null, null, null);
  }

  public Diary(User user, Category category, String title, String content, String photoUrl, String price) {
    this(null, user, category, title, content, null, photoUrl, price);
  }

  public Diary(Long id, User user, Category category, String title, String content, LocalDateTime createAt, String photoUrl, String price) {
    checkArgument(user != null, "user must be provided.");
    checkArgument(category != null, "category must be provided.");
    checkArgument(title != null, "title must be provided.");
    checkArgument(
      title.length() >= 1 && title.length() <= 30,
      "title length must be between 1 and 30 characters."
    );

    this.id = id;
    this.user = user;
    this.category = category;
    this.title = title;
    this.content = content;
    // 지정된 시각이 없으면 현재 시각으로 설정
    this.createAt = defaultIfNull(createAt, now());
    this.photoUrl = photoUrl;
    this.price = price;
  }
  
  /**
   * 개별 수정이 필요한 필드들에 대해서만 setter 생성
   */
  public void setCategory(Category category) {
    this.category = category;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Diary diary = (Diary) o;
    return price == diary.price && Objects.equals(id, diary.id) &&
      Objects.equals(user, diary.user) && Objects.equals(category, diary.category) &&
      Objects.equals(title, diary.title) && Objects.equals(content, diary.content) &&
      Objects.equals(createAt, diary.createAt) && Objects.equals(photoUrl, diary.photoUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, category, title, content, createAt, photoUrl, price);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
