package com.august.soil.api.controller.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@NoArgsConstructor
@Getter @Setter
public class UpdateDiaryRequest {
  
  @NotNull
  @ApiModelProperty(value = "카테고리 PK. 변화 없으면 기존값 그대로 보냄", required = true)
  private Long categoryId;
  
  @NotEmpty
  @ApiModelProperty(value = "수정할 제목. 변화 없으면 기존값 그대로 보냄", required = true)
  private String title;
  
  @NotEmpty
  @ApiModelProperty(value = "수정할 내용. 변화 없으면 기존값 그대로 보냄", required = true)
  private String content;
  
  @NotEmpty
  @ApiModelProperty(value = "수정할 가격. 변화 없으면 기존값 그대로 보냄", required = true)
  private String price;
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
