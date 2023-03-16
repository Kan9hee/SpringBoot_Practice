package practice.web.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import practice.web.domain.member.Member;

class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){
        MockHttpServletResponse response = new MockHttpServletResponse();
        // HttpServletResponse는 인터페이스인 관계로, 이를 테스트하는 데 도움을 주는
        // MockHttpServletResponse를 사용한다.

        Member member = new Member();
        sessionManager.createSession(member,response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        // 마찬가지로 MockHttpServletRequest을 통해 요청된 쿠키를 저장하는 상황을 가정한다.

        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        sessionManager.expire(request);
        Object expired=sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }
}