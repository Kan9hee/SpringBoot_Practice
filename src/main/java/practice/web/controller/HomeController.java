package practice.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import practice.web.argumentresolver.Login;
import practice.web.domain.member.Member;
import practice.web.domain.member.MemberRepository;
import practice.web.session.SessionConst;
import practice.web.session.SessionManager;

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

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        Member member = (Member) sessionManager.getSession(request);
        // 세션 관리자 내의 회원 정보 조회

        if(member==null){
            return "view/home";
        }

        model.addAttribute("member",member);
        return "view/loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        HttpSession session=request.getSession(false);
        if(session==null){
            return "view/home";
        }

        Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(loginMember==null){
            return "view/home";
        }

        model.addAttribute("member",loginMember);
        return "view/loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false)Member member, Model model){
        // @SessionAttribute를 통해 세션에서 getAttribute하는 과정을 쉽게 해결할 수 있다.

        if(member==null){
            return "view/home";
        }

        model.addAttribute("member",member);
        return "view/loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){

        if(loginMember==null){
            return "view/home";
        }

        model.addAttribute("member",loginMember);
        return "view/loginHome";
    }
}
