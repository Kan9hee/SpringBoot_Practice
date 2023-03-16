package practice.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOG_ID="logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid= UUID.randomUUID().toString();

        request.setAttribute(LOG_ID,uuid);

        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            // 호출할 컨트롤러 메서드의 모든 정보 포함
        }

        log.info("REQUEST [{}][{}][{}][{}]",uuid,request.getDispatcherType(),requestURI,handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        Object logId = request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]",logId,requestURI,handler);
        if(ex!=null){
            log.error("afterCompletion error",ex);
        }
    }
}
// 인터셉터는 필터 구간이 3개로, 컨트롤러 호출 전, 호출 후, 요청완료 후로 나뉜다.
// 각각의 실행 시점이 다르기에, request에 preHandle의 값을 담아 postHandle과 afterCompletion에서 사용할 수 있도록 한다.
// 서블릿이 제공하는 URL보다 더 세밀하다.