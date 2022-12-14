package com.august.soil.api.configure;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * application.yml 에서 JWT 설정 정보를 읽어온다.
 */
@Component
@ConfigurationProperties(prefix = "jwt.token")
@Getter @Setter
public class JwtTokenConfigure {
  
  private String header;
  
  private String issuer;
  
  private String clientSecret;
  
  private int expirySeconds;
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
