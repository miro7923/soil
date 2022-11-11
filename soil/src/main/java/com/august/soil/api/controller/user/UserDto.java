package com.august.soil.api.controller.user;

import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.SnsType;
import com.august.soil.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.springframework.beans.BeanUtils.copyProperties;

@NoArgsConstructor
@Getter @Setter
public class UserDto {
  
  @ApiModelProperty(value = "PK", required = true)
  private Long id;
  
  @ApiModelProperty(value = "소셜 로그인 종류(네이버, 카카오 등)", required = true)
  private SnsType snsType;
  
  @ApiModelProperty(value = "사용자 이름", required = true)
  private String name;
  
  @ApiModelProperty(value = "소셜 로그인 서비스에 등록된 사용자 이메일", required = true)
  private Email email;
  
  @ApiModelProperty(value = "가입 일자", required = true)
  private LocalDateTime createAt;
  
  public UserDto(User source) {
    copyProperties(source, this);
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
