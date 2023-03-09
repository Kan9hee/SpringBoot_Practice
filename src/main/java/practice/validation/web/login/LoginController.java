package practice.validation.web.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import practice.validation.domain.login.LoginService;
import practice.validation.domain.member.Member;

import java.net.http.HttpResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form){
        return "view/login/loginForm";
    }

    @PostMapping("/login")
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

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie=new Cookie("memberId",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
