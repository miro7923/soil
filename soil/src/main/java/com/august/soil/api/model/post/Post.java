package com.august.soil.api.model.post;

import com.august.soil.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.time.LocalDateTime.*;
import static javax.persistence.FetchType.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
public class Post {

  @Id @GeneratedValue
  @Column(name = "post_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private final User user;

  @Column(nullable = false)
  private final String content;

  @Column(nullable = false)
  private final LocalDateTime createAt;
  
  private final int readcount;

  public Post(User user, String content) {
    this(null, user, content, null, 0);
  }
  public Post(Long id, User user, String content, LocalDateTime createAt, int readcount) {
    checkArgument(user != null, "user must be provided.");
    checkArgument(content != null, "content must be provided.");
    
    this.id = id;
    this.user = user;
    this.content = content;
    this.createAt = defaultIfNull(createAt, now());
    this.readcount = readcount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Post post = (Post) o;
    return readcount == post.readcount && Objects.equals(id, post.id) &&
      Objects.equals(user, post.user) && Objects.equals(content, post.content) &&
      Objects.equals(createAt, post.createAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, content, createAt, readcount);
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
    private String content;
    private LocalDateTime createAt;
    private int readcount;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
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

    public Builder readcount(int readcount) {
      this.readcount = readcount;
      return this;
    }

    public Post build() {
      return new Post(id, user, content, createAt, readcount);
    }
  }
}
