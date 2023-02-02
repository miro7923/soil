package com.august.soil.api.model.post;

import com.august.soil.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.LAZY;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
@Getter
@Builder
public class Post {

  @Id @GeneratedValue
  @Column(name = "post_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
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
    return reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
