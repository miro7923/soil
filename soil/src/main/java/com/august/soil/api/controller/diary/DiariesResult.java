package com.august.soil.api.controller.diary;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DiariesResult<T> {
  
  @NotNull
  @ApiModelProperty(value = "사용자 PK")
  private Long userId;
  
  @NotNull
  @ApiModelProperty(value = "조회된 전체 일기 개수", required = true)
  private int count;
  
  @NotNull
  @ApiModelProperty(value = "일기 리스트", required = true)
  private T diaryList;
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
