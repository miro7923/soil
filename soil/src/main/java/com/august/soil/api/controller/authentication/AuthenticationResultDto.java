package com.august.soil.api.controller.authentication;

import com.august.soil.api.controller.user.UserDto;
import com.august.soil.api.security.AuthenticationResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.springframework.beans.BeanUtils.copyProperties;

@Getter @Setter
public class AuthenticationResultDto {
  
  @ApiModelProperty(value = "API 토큰", required = true)
  private String apiToken;
  
  @ApiModelProperty(value = "사용자 정보", required = true)
  private UserDto user;
  
  public AuthenticationResultDto(AuthenticationResult source) {
    copyProperties(source, this);
  
    this.user = new UserDto(source.getUser());
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
