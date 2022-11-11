package com.august.soil.api.controller.authentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@NoArgsConstructor
@Getter @Setter
public class IssueTokenRequest {
  
  @NotEmpty
  @ApiModelProperty(value = "네이버한테서 발급받은 접근 토큰(AccessToken)", required = true)
  private String naverAccessToken;
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
