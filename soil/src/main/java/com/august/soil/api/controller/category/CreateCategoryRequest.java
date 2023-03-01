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
public class CreateCategoryRequest {

  @NotEmpty
  @ApiModelProperty(value = "새로 작성할 카테고리명", required = true)
  private String name;

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
