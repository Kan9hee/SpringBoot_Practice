package practice.validation.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.validation.domain.member.Member;
import practice.validation.domain.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password){
        /*
        Optional<Member> findMemberOptional= memberRepository.findByLoginId(loginId);
        Member member=findMemberOptional.get();
        if(member.getPassword().equals(password)){
            return member;
        }else{
            return null;
        }
        // Optional은 NullPointerException을 방지해준다.
        */

        return memberRepository.findByLoginId(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
        // 로그인을 시도한 계정의 아이디와 비빌번호 일치시 반환한다. 아닐 경우 null을 반환한다.
    }
}
