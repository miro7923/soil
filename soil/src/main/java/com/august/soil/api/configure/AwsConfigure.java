package com.august.soil.api.configure;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Getter @Setter
public class AwsConfigure {
  
  private String accessKey;
  
  private String secretKey;
  
  private String region;
  
  private String url;
  
  private String bucketName;
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
