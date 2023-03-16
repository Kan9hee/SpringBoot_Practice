package practice.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest=(HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid= UUID.randomUUID().toString();
        try{
            log.info("REQUEST [{}][{}][{}]",uuid,request.getDispatcherType(),requestURI);
            chain.doFilter(request,response);
        }catch (Exception e){
            log.info("EXCEPTION {}",e.getMessage());
            throw e; // WAS에 예외사항 전달
        }finally {
            log.info("RESPONSE [{}][{}][{}]",uuid,request.getDispatcherType(),requestURI);
        }

        // 모든 요청을 로그에 남기는 필터.
        // http 요청시 doFilter가 순환호출된다. uuid와 requestURI를 출력하게 된다.
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
