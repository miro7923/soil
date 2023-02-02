package com.august.soil.api.controller.user;

import com.august.soil.api.controller.ApiResult;
import com.august.soil.api.error.NotFoundException;
import com.august.soil.api.model.user.User;
import com.august.soil.api.security.JwtAuthentication;
import com.august.soil.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Api(tags = "사용자 APIs")
public class UserRestController {

  private final UserService userService;

  /**
   * 회원 한 명의 정보 조회
   * @param authentication(토큰 헤더에 넣어서 보낼 것)
   * @return ApiResult<UserDto>
   */
  @GetMapping("/users/me")
  @ApiOperation(value = "사용자 정보 조회")
  public ApiResult<UserDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
    return ApiResult.OK(
      userService.findById(authentication.id)
        .map(UserDto::new)
        .orElseThrow(() -> new NotFoundException(User.class, authentication.id))
    );
  }

  @DeleteMapping("/users/remove")
  @ApiOperation(value = "회원탈퇴. 성공응답을 받고나면 클라이언트단에 저장된 토큰 삭제할 것")
  public ApiResult deleteUser(@AuthenticationPrincipal JwtAuthentication authentication) {
    boolean result = userService.deleteUser(authentication.id);
    String msg = result ? "회원탈퇴 성공" : "회원정보 없음";
    return ApiResult.OK(msg);
  }

  /**
   * 회원 전체 목록 조회
   * @return 가입된 회원 전체의 login id
   */
//  @GetMapping("/users/list")
//  public MemberResult getMembers() {
//    List<User> findMembers = userService.findUsers();
//    List<MemberDto> collect = findMembers.stream()
//      .map(m -> new MemberDto(m.getId(), m.getNickname()))
//      .collect(Collectors.toList());
//
//    return new MemberResult(findMembers.size(), collect);
//  }
}
