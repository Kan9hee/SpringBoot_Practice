package practice.validation.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import practice.validation.domain.member.Member;
import practice.validation.domain.member.MemberRepository;
import practice.validation.web.session.SessionManager;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model){

        if(memberId==null){
            return "view/home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if(loginMember==null){
            return "view/home";
        }

        model.addAttribute("member",loginMember);
        return "view/loginHome";
    }

    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        Member member = (Member) sessionManager.getSession(request);
        // 세션 관리자 내의 회원 정보 조회

        if(member==null){
            return "view/home";
        }

        model.addAttribute("member",member);
        return "view/loginHome";
    }
}
