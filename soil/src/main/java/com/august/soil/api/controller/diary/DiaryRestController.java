package com.august.soil.api.controller.diary;

import com.august.soil.api.controller.ApiResult;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.JwtAuthentication;
import com.august.soil.api.service.category.CategoryService;
import com.august.soil.api.service.diary.DiaryService;
import com.august.soil.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.august.soil.api.controller.ApiResult.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Api(tags = "사용자 일기 APIs - 토큰에 사용자의 id 정보가 들어있기 때문에 URL에 따로 붙여서 보낼 필요 없음")
public class DiaryRestController {

  private final DiaryService diaryService;
  private final UserService userService;
  private final CategoryService categoryService;

  /**
   * 사용자 한 명의 일기 목록을 응답
   *
   * @param Jwt token(authentication)
   * @return id에 해당하는 회원의 일기 전체 목록
   */
  @GetMapping("/diaries/list")
  @ApiOperation(value = "사용자의 전체 일기 조회")
  public ApiResult<DiariesResult<List<DiaryDto>>> diaries(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {
    List<Diary> findDiaries = diaryService.findDiaries(authentication.id);
    Optional<User> findUser = userService.findById(authentication.id);
    Category findCategory = categoryService.findCategory(1L);

    List<DiaryDto> collect = findDiaries.stream()
      .map(DiaryDto::new)
      .collect(Collectors.toList());

    return ApiResult.OK(
      new DiariesResult<>(collect.size(), collect)
    );
  }

  @PostMapping("/diaries/upload")
  @ApiOperation(value = "일기 작성")
  public ApiResult<com.august.soil.api.controller.diary.DiaryDto> uploadDiary(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody @Valid CreateDiaryRequest param
  ) {
    Optional<User> member = userService.findById(authentication.id);
    // TODO : 카테고리 ID 파라미터 리팩토링 하기
    Category category = categoryService.findCategory(param.getCategory_id());

    return OK(
      new com.august.soil.api.controller.diary.DiaryDto(
        diaryService.upload(
          // TODO : Optional member 유효성 검사 후 파라미터로 넣게 수정
          new Diary(member.get(), category, param.getTitle(), param.getContent(), param.getPrice()))
      )
    );
  }

  @GetMapping("/diaries/{diaryId}")
  @ApiOperation(value = "사용자의 일기 하나 조회")
  public ApiResult<?> getDiary(@PathVariable(value = "diaryId") Long diaryId) {
    Optional<Diary> diary = diaryService.findDiary(Id.of(Diary.class, diaryId));

    if (diary.isEmpty()) {
      return ERROR("해당하는 일기를 찾을 수 없습니다. 일기의 ID를 확인해 주세요.", HttpStatus.NOT_FOUND);
    }

    return OK(new DiaryDto(diary.get()));
  }

  @PatchMapping("/diaries/{diaryId}")
  @ApiOperation(value = "작성한 일기 수정")
  public ApiResult updateDiary(
    @PathVariable(value = "diaryId") Long diaryId,
    @RequestBody @Valid UpdateDiaryRequest request
  ) {
    log.debug("request: {}", request.toString());
    if (!diaryService.updateDiary(Id.of(Diary.class, diaryId), request)) {
      return ERROR("일기 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK(
      diaryService.findDiary(Id.of(Diary.class, diaryId)).get()
    );
  }

  @DeleteMapping("/diaries/{diaryId}")
  @ApiOperation(value = "작성한 일기 삭제")
  public ApiResult deleteDiary(@PathVariable(value = "diaryId") Long diaryId) {
    if (!diaryService.deleteDiary(Id.of(Diary.class, diaryId))) {
      return ERROR("일기 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK("일기 삭제 성공");
  }
}
