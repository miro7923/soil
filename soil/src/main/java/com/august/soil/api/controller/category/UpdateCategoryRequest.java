package com.august.soil.api.controller.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@NoArgsConstructor
@Getter
@Setter
public class UpdateCategoryRequest {

  @NotEmpty
  @ApiModelProperty(value = "원래 카테고리명", required = true)
  private String originName;

  @NotEmpty
  @ApiModelProperty(value = "수정할 카테고리명. 변화 없으면 기존값 그대로 보냄", required = true)
  private String newName;

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
