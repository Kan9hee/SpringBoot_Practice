package practice.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME="mySessionId";

    private Map<String, Object> sessionStore=new ConcurrentHashMap<>();
    // 동시성 이슈로 인해 HashMap 대신 ConcurrentHashMap을 쓰는 편이 졸다.

    public void createSession(Object value, HttpServletResponse response){

        String sessionId= UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);
        // 세션 id 생성 및 값 저장

        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME,sessionId);
        response.addCookie(mySessionCookie);
        // 쿠키 생성
    }

    public Object getSession(HttpServletRequest request){

        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie==null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
        // 세션 조회 후 쿠키 반환
    }

    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
        // 현재 세션의 쿠키 삭제
    }

    public Cookie findCookie(HttpServletRequest request,String cookieName){
        if(request.getCookies()==null){
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}

// 세션 관리용 클래스
