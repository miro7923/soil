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

/**
 * API 응답 결과로 리턴할 때 사용할 DTO
 * 때문에 Swagger에는 이 클래스의 필드를 기준으로 작성되어야 한다.
 * @ApiModelProperty 어노테이션의 필드에 데이터를 작성하면 Swagger 문서에 반영된다.
 */
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
