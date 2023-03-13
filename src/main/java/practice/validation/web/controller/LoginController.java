package practice.validation.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practice.validation.domain.login.LoginService;
import practice.validation.domain.member.Member;
import practice.validation.web.login.LoginForm;
import practice.validation.web.session.SessionConst;
import practice.validation.web.session.SessionManager;

import java.net.http.HttpResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "view/login/loginForm";
    }

    //@PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){

        if(bindingResult.hasErrors()){
            return "view/login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 혹은 비밀번호 불일치 오류");
            return "view/login/loginForm";
        }

        Cookie cookie = new Cookie("memberId",String.valueOf(loginMember.getId()));
        response.addCookie(cookie);
        // 로그인 성공시 세션 쿠키를 생성한다.
        // 다만 클라이언트가 쿠키 값을 임의로 변경하거나 정보를 가져갈 수도 있다.
        // 한번 가져간 쿠키는 계속 쓸 수 있어, 개선이 필요하다.

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){

        if(bindingResult.hasErrors()){
            return "view/login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 혹은 비밀번호 불일치 오류");
            return "view/login/loginForm";
        }

        sessionManager.createSession(loginMember,response);
        // 쿠키의 값을 공개하지 않고, 임의의 값을 쿠키와 대응하는 토큰으로 사용한다.
        // 또한 쿠키의 유효시간을 제한하고, 의심되는 경우 해당 토큰을 강제로 제거한다.
        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관한다.

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "view/login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 혹은 비밀번호 불일치 오류");
            return "view/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        // 서븛릿의 HttpSession을 통해 신슈 세션 생성 및 기존 세션 반환, 회원 정보 보관을 한다.
        // request.getSession()에 어떤 값을 설정하는냐에 따라 동작이 달라진다.
        //      세션 있을시: 기존 세션을 반환한다.
        //      세션 없을시: true면 새로운 세션을 생성 및 반환한다.
        //                 false면 생성하지 않고 null을 반환한다.

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm form,
                          BindingResult bindingResult,
                          HttpServletRequest request,
                          @RequestParam(defaultValue = "/") String redirectURL){

        if(bindingResult.hasErrors()){
            return "view/login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 혹은 비밀번호 불일치 오류");
            return "view/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:"+redirectURL;
        // 로그인시 현재 위치에서 로그인된 결과를 반환한다.
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie=new Cookie("memberId",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }
}
