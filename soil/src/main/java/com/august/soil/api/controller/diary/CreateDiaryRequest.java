package com.august.soil.api.controller.diary;

import com.august.soil.api.model.diary.Diary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.builder.ToStringBuilder.*;
import static org.springframework.beans.BeanUtils.copyProperties;

@NoArgsConstructor
@Getter @Setter
public class CreateDiaryRequest {

  @NotNull
  @ApiModelProperty(value = "카테고리 PK", required = true)
  private Long categoryId;
  
  @NotEmpty
  @ApiModelProperty(value = "새로 작성할 일기 제목", required = true)
  private String title;
  
  @NotEmpty
  @ApiModelProperty(value = "새로 작성할 일기 내용", required = true)
  private String content;
  
  @NotEmpty
  @ApiModelProperty(value = "상품 가격", required = true)
  private String price;
  
  public CreateDiaryRequest(Diary source) {
    copyProperties(source, this);
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
