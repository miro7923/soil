package com.august.soil.api.controller.category;

import com.august.soil.api.controller.ApiResult;
import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.JwtAuthentication;
import com.august.soil.api.service.category.CategoryService;
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

import static com.august.soil.api.controller.ApiResult.ERROR;
import static com.august.soil.api.controller.ApiResult.OK;

/**
 *  JWTauthenticaion 접속 회원의 토큰을 이용해서 회원마다 자신의 카테고리 crud가능
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Api(tags = "카테고리 APIs - 토큰에 사용자의 categoryId 정보가 들어있기 때문에 URL에 따로 붙여서 보낼 필요 없음")
public class CategoryRestController {
  private final UserService userService;
  private final CategoryService categoryService;

  @PostMapping(value = "/category/add")
  @ApiOperation(value = "카테고리 등록")
  public ApiResult<?> uploadCategory(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody @Valid CreateCategoryRequest request
  ) {

    Optional<User> user = userService.findById(authentication.id);

    if (user.isEmpty()) {
      return ERROR("로그인 정보가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    return OK(
      new CategoryDto(
        categoryService.add(
          new Category(user.get(), request.getName())
        )
      )
    );
  }

  @GetMapping("/category/categoryList")
  @ApiOperation(value = "전체 카테고리 조회")
  public ApiResult<CategoryResult<List<CategoryDto>>> categories(
    @AuthenticationPrincipal JwtAuthentication authentication
  ) {

    List<Category> findCategory = categoryService.findCategories(authentication.id);
    List<CategoryDto> categoryList = findCategory.stream()
      .map(CategoryDto::new)
      .collect(Collectors.toList());

    return OK(
      new CategoryResult<>(categoryList.size(), categoryList)
    );
  }

  @PatchMapping("/category/update")
  @ApiOperation(value = "카테고리 이름 수정")
  public ApiResult<?> updateCategory(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody @Valid UpdateCategoryRequest request
  ) {

    List<Category> category = categoryService.findByName(authentication.id, request.getOriginName());
    if (category.isEmpty()) {
      return ERROR("카테고리 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    Category newCategory = category.get(0);
    newCategory.setName(request.getNewName());
    if (!categoryService.updateCategory(newCategory)) {
      return ERROR("카테고리 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK("카테고리명 수정 성공");
  }

  @DeleteMapping("/category/remove")
  @ApiOperation(value = "카테고리 삭제")
  public ApiResult<?> deleteCategory(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody @Valid RemoveCategoryRequest request
  ) {

    List<Category> category = categoryService.findByName(authentication.id, request.getName());
    if (category.isEmpty() ||
      !categoryService.deleteCategory(Id.of(Category.class, category.get(0).getId()))
    ) {
      return ERROR("카테고리 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    return OK("카테고리 삭제 성공");
  }

}