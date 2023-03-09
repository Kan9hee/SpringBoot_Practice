package practice.validation.web.controller;

import jakarta.servlet.http.Cookie;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
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
}
