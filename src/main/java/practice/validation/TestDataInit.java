package practice.validation;

import practice.validation.web.domain.item.Item;
import practice.validation.web.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.validation.web.domain.member.Member;
import practice.validation.web.domain.member.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

        Member member=new Member();
        member.setLoginId("id");
        member.setPassword("password!");
        member.setName("testAccount");

        memberRepository.save(member);
    }

}