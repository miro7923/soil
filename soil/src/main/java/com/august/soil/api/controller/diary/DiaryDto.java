package com.august.soil.api.controller.diary;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.diary.Diary;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * API 응답 결과로 리턴할 때 사용할 DTO
 * 때문에 Swagger에는 이 클래스의 필드를 기준으로 작성되어야 한다.
 * @ApiModelProperty 어노테이션의 필드에 데이터를 작성하면 Swagger 문서에 반영된다.
 */
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
    return reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
