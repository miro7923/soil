package com.august.soil.api.controller.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResult<C> {

  @NotNull
  @ApiModelProperty(value = "조회된 전체 카테고리 개수", required = true)
  private int count;

  @NotNull
  @ApiModelProperty(value = "카테고리 리스트", required = true)
  private C categoryList;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}