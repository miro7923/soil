package august.soil.web.session;

import august.soil.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() throws Exception {
        // given
        // 세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        // when
        // 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // then
        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        // 세션만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}
