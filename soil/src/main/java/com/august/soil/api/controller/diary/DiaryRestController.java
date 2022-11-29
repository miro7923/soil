package com.august.soil.api.controller.diary;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.august.soil.api.aws.S3Client;
import com.august.soil.api.configure.support.Pageable;
import com.august.soil.api.controller.ApiResult;
import com.august.soil.api.model.commons.AttachedFile;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.JwtAuthentication;
import com.august.soil.api.service.category.CategoryService;
import com.august.soil.api.service.diary.DiaryService;
import com.august.soil.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.august.soil.api.controller.ApiResult.*;
import static com.august.soil.api.model.commons.AttachedFile.toAttachedFile;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Api(tags = "사용자 일기 APIs - 토큰에 사용자의 id 정보가 들어있기 때문에 URL에 따로 붙여서 보낼 필요 없음")
public class DiaryRestController {

  private final DiaryService diaryService;
  private final UserService userService;
  private final CategoryService categoryService;
  private final S3Client s3Client;
  
  /**
   * 사용자 한 명의 일기 목록을 응답
   *
   * @param JWT token(authentication)
   * @return id에 해당하는 회원의 일기 전체 목록
   */
  @GetMapping("/diaries/list")
  @ApiOperation(value = "사용자의 전체 일기 조회")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "offset", dataTypeClass = Integer.class, paramType = "query", defaultValue = "0", value = "페이징 offset"),
    @ApiImplicitParam(name = "limit", dataTypeClass = Integer.class, paramType = "query", defaultValue = "10", value = "최대 조회 갯수(max = 10)")
  })
  public ApiResult<DiariesResult<List<DiaryDto>>> diaries(
    @AuthenticationPrincipal JwtAuthentication authentication, Pageable pageable
  ) {
    List<Diary> findDiaries = diaryService.findDiaries(authentication.id, pageable.offset(), pageable.limit());
//    Optional<User> findUser = userService.findById(authentication.id);
//    Optional<Category> findCategory = categoryService.findCategory(Id.of(Category.class, 1L));

    List<DiaryDto> collect = findDiaries.stream()
      .map(DiaryDto::new)
      .collect(Collectors.toList());

    return OK(
      new DiariesResult<>(collect.size(), collect)
    );
  }

  @PostMapping(value = "/diaries/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation(value = "일기 작성")
  public ApiResult<?> uploadDiary(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @ModelAttribute CreateDiaryRequest param,
    @RequestPart(required = false) MultipartFile file
    ) throws IOException {
    Optional<User> user = userService.findById(authentication.id);
    Optional<Category> category = categoryService.findCategory(Id.of(Category.class, param.getCategory_id()));
//    Diary diary = diaryService.upload(
//      // TODO : Optional member 유효성 검사 후 파라미터로 넣게 수정
//      new Diary(member.get(), category.get(), param.getTitle(), param.getContent(), param.getPrice())
//    );
  
    // TODO : 비동기 업로드 처리 좀 더 공부해 보고 완성하기!!!!
//    Diary diary = new Diary(user.get(), category.get(), param.getTitle(), param.getContent(), null, param.getPrice());
////    AtomicReference<Diary> diary = new AtomicReference<>();
//    toAttachedFile(file).ifPresent(attachedFile -> {
//      log.info("toAttachedFile(file).ifPresent(attachedFile");
//        supplyAsync(() -> uploadDiaryPhoto(attachedFile)).thenAccept(p -> {
//          p.ifPresent(photoUrl -> {
//            log.debug("@@@@@@@@@@@ p: {}", p.get());
//            diaryService.upload(
//              new Diary(user.get(), category.get(), param.getTitle(), param.getContent(), p.get(), param.getPrice())
//            );
//          });
//        }).exceptionally(e -> {
//          throw new RuntimeException("사진 업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.", e);
////        return e.getMessage();
////        return ERROR("사진 업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);
//        });
//      }
//    );
//
////    if (diary.get() == null)
////      return () -> ERROR("사진 업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);
//
//    return () -> OK(
//      new CompletableFuture<>()
//    );
  
    Optional<String> photoUrl = uploadDiaryPhoto(new AttachedFile(file.getOriginalFilename(), file.getContentType(), file.getBytes()));
    if (photoUrl.isEmpty())
      return ERROR("사진 업로드 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.", HttpStatus.BAD_REQUEST);

    return OK(
      new DiaryDto(
        diaryService.upload(
          new Diary(user.get(), category.get(), param.getTitle(), param.getContent(), photoUrl.get(), param.getPrice())
        )
      )
    );
  }
  
  private Optional<String> uploadDiaryPhoto(AttachedFile photoFile) {
    String photoUrl = null;
    if (photoFile != null) {
      String key = photoFile.randomName(null, "jpeg");
      try {
        photoUrl = s3Client.upload(photoFile.inputStream(), photoFile.length(), key, photoFile.getContentType(), null);
      } catch (AmazonS3Exception e) {
        log.warn("Amazon S3 error : (key : {}): {}", key, e.getMessage(), e);
      }
    }
    return ofNullable(photoUrl);
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
  public ApiResult<?> updateDiary(
    @PathVariable(value = "diaryId") Long diaryId,
    @RequestBody @Valid UpdateDiaryRequest request
  ) {
    log.debug("request: {}", request.toString());
    Optional<Diary> diary = diaryService.findDiary(Id.of(Diary.class, diaryId));
    if (!diaryService.updateDiary(Id.of(Diary.class, diaryId), request)) {
      return ERROR("일기 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK(
      diaryService.findDiary(Id.of(Diary.class, diaryId)).get()
    );
  }

  @DeleteMapping("/diaries/{diaryId}")
  @ApiOperation(value = "작성한 일기 삭제")
  public ApiResult<?> deleteDiary(@PathVariable(value = "diaryId") Long diaryId) {
    deletePhoto(Id.of(Diary.class, diaryId));
    if (!diaryService.deleteDiary(Id.of(Diary.class, diaryId))) {
      return ERROR("일기 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK("일기 삭제 성공");
  }
  
  private void deletePhoto(Id<Diary, Long> id) {
    Optional<Diary> diary = diaryService.findDiary(id);
    log.info("diary to delete: {}", diary);
    diary.ifPresent(value ->
      s3Client.delete(value.getPhotoUrl())
    );
  }
}
