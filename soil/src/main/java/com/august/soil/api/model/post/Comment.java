package com.august.soil.api.model.post;

import com.august.soil.api.model.user.User;
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
public class Comment {

  @Id @GeneratedValue
  @Column(name = "comment_id")
  private final Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private final User user;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private final Post post;

  @Column(nullable = false)
  private final String content;
  private final LocalDateTime createAt;

  public Comment(User user, Post post, String content) {
    this(null, user, post, content, null);
  }

  public Comment(Long id, User user, Post post, String content, LocalDateTime createAt) {
    checkArgument(user != null, "user must be provided.");
    checkArgument(post != null, "post must be provided.");
    checkArgument(content != null, "content must be provided.");
    checkArgument(
        content.length() >= 10 && content.length() <= 100,
        "content length must be between 10 and 100 characters."
    );

    this.id = id;
    this.user = user;
    this.post = post;
    this.content = content;
    this.createAt = defaultIfNull(createAt, now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id) && Objects.equals(user, comment.user) &&
      Objects.equals(post, comment.post) && Objects.equals(content, comment.content) &&
      Objects.equals(createAt, comment.createAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, post, content, createAt);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
