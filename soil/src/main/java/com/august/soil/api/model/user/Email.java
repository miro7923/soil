package com.august.soil.api.model.user;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * 사용자 email의 유효성 검증과 비즈니스 로직 사용의 편의성을 위해 따로 선언
 * @Embeddable을 통해 User 클래스와 값 타입으로 매핑된다.
 */
@Getter
@Embeddable
@Builder
public class Email implements Serializable {

  private final String email;
  
  public Email() {
    email = null;
  }

  // 객체 생성 전에 유효성 검증이 먼저 이뤄진 후 생성될 수 있도록 한다.
  public Email(String email) {
    checkArgument(isNotEmpty(email), "address must be provided.");
    checkArgument(
        email.length() >= 4 && email.length() <= 50,
        "address length must be between 4 and 50 characters."
    );
    checkArgument(checkAddress(email), "Invalid email address: " + email);

    this.email = email;
  }

  private static boolean checkAddress(String address) {
    return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
  }

  public String getName() {
    String[] tokens = email.split("@");
    if (tokens.length == 2) return tokens[0];
    return null;
  }

  public String getDomain() {
    String[] tokens = email.split("@");
    if (tokens.length == 2) return tokens[1];
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return Objects.equals(this.email, email.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
