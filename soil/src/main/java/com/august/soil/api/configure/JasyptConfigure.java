package com.august.soil.api.configure;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Component
@ConfigurationProperties(prefix = "jasypt.encryptor")
@Getter @Setter
public class JasyptConfigure {
  
  private String password;
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
