package com.august.soil.api.controller.authentication;

import com.august.soil.api.controller.user.UserDto;
import com.august.soil.api.security.AuthenticationResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * API 응답 결과로 리턴할 때 사용할 DTO
 * 때문에 Swagger에는 이 클래스의 필드를 기준으로 작성되어야 한다.
 * @ApiModelProperty 어노테이션의 필드에 데이터를 작성하면 Swagger 문서에 반영된다.
 */
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
