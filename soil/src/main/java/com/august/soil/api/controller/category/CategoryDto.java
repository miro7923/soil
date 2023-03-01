package com.august.soil.api.controller.category;

import com.august.soil.api.model.category.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.springframework.beans.BeanUtils.copyProperties;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

  @NotNull
  @ApiModelProperty(value = "PK", required = true)
  private Long id;

  @NotNull
  @ApiModelProperty(value = "카테고리 이름", required = true)
  private String name;

  public CategoryDto(Category source) {
    copyProperties(source, this);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
