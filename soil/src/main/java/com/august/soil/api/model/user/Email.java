package com.august.soil.api.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Getter
@Embeddable
public class Email {

  private final String email;
  
  public Email() {
    email = null;
  }

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
  
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Builder {
    private String email;

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Email build() {
      return new Email(email);
    }
  }
}
