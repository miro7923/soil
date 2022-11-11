package com.august.soil.api.model.diary;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
public class Diary {

  @Id @GeneratedValue
  @Column(name = "diary_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private final User user;

  @ManyToOne(fetch = EAGER)
  @JoinColumn(name = "category_id")
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

  public Diary(User user, Category category, String title, String content, String price) {
    this(null, user, category, title, content, null, null, price);
  }

  public Diary(Long id, User user, Category category, String title, String content, LocalDateTime createAt, String photoUrl, String price) {
    checkArgument(user != null, "user must be provided.");
    checkArgument(category != null, "category must be provided.");
    checkArgument(title != null, "title must be provided.");
    checkArgument(
      title.length() >= 1 && title.length() <= 20,
      "title length must be between 1 and 20 characters."
    );

    this.id = id;
    this.user = user;
    this.category = category;
    this.title = title;
    this.content = content;
    this.createAt = defaultIfNull(createAt, now());
    this.photoUrl = photoUrl;
    this.price = price;
  }
  
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

  @AllArgsConstructor
  @NoArgsConstructor
  static public class Builder {
    private Long id;
    private User user;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private String photoUrl;
    private String price;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder createAt(LocalDateTime createAt) {
      this.createAt = createAt;
      return this;
    }

    public Builder photoUrl(String photoUrl) {
      this.photoUrl = photoUrl;
      return this;
    }

    public Builder price(String price) {
      this.price = price;
      return this;
    }

    public Diary build() {
      return new Diary(id, user, category, title, content, createAt, photoUrl, price);
    }
  }
}
