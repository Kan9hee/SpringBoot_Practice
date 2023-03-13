package practice.validation.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import practice.validation.web.session.SessionConst;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist={"/","/members/add","/login","/logout","/css/*"};
    // 로그인이 되지 않은 상태에서도 접속할 수 있도록 일부 경로를 풀어준다.

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest=(HttpServletRequest) request;
        String requestURI=httpRequest.getRequestURI();

        HttpServletResponse httpResponse=(HttpServletResponse) response;

        try{
            log.info("login check filter start{}",requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("login check logic start{}",requestURI);
                HttpSession session=httpRequest.getSession(false);

                if(session==null||session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                    log.info("uncertified user request{}",requestURI);
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
                // 로그인 실패시 현재 페이지 정보를 로그인 페이지에 돌려준다.
            }
            chain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("login check filter end{}",requestURI);
        }
    }

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI);
        // 화이트리스트 내의 경로는 로그인 체크를 하지 않아야 하므로, 결과값의 부정형을 반환한다.
    }
}
// init과 destroy는 default메서드가 있어 구현하지 않아도 된다.