package com.august.soil.api.controller.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
@Setter
public class RemoveCategoryRequest {

  @NotEmpty
  @ApiModelProperty(value = "삭제할 카테고리명", required = true)
  private String name;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
