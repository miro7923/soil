package com.august.soil.api.controller.diary;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.builder.ToStringBuilder.*;
import static org.springframework.beans.BeanUtils.copyProperties;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DiaryDto {
  
  @NotNull
  @ApiModelProperty(value = "PK", required = true)
  private Long id;
  
  @NotNull
  @ApiModelProperty(value = "카테고리 정보", required = true)
  private Category category;
  
  @NotEmpty
  @ApiModelProperty(value = "일기 제목", required = true)
  private String title;
  
  @NotEmpty
  @ApiModelProperty(value = "일기 내용", required = true)
  private String content;
  
  @ApiModelProperty(value = "작성 날짜")
  private LocalDateTime createAt;
  
  @ApiModelProperty(value = "업로드 한 사진 URL")
  private String photoUrl;
  
  @NotEmpty
  @ApiModelProperty(value = "상품 가격", required = true)
  private String price;
  
  public DiaryDto(Diary source) {
    copyProperties(source, this);
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
