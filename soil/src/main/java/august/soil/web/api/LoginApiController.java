package august.soil.web.api;

import august.soil.domain.Member;
import august.soil.request.LoginMemberRequest;
import august.soil.response.LoginMemberResponse;
import august.soil.response.LogoutMemberResponse;
import august.soil.service.LoginService;
import august.soil.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/members/login")
    public LoginMemberResponse login(@RequestBody @Valid LoginMemberRequest param, HttpServletResponse response,
                                     HttpServletRequest request) {
        Member loginMember = loginService.login(param.getLoginId(), param.getPassword());
        if (loginMember == null) {
            return new LoginMemberResponse(-1, "존재하지 않는 회원이거나 비밀번호가 일치하지 않습니다.");
        } else {
            // 로그인 성공 처리
            // 세션이 있으면 세션 반환, 없으면 신규 세션 생성
            HttpSession session = request.getSession();
            // 세션에 로그인 회원 정보 보관
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

            return new LoginMemberResponse(1, "로그인 성공");
        }
    }

    @PostMapping("/members/logout")
    public LogoutMemberResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 로그아웃인데 세션 없을 때 생성 안 함
        if (session != null) {
            session.invalidate();
        }
        return new LogoutMemberResponse(1, "로그아웃 성공");
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
