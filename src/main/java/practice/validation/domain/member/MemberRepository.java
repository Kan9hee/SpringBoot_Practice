package practice.validation.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence=0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}",member);
        store.put(member.getId(), member);
        return member;
    }
    // 계정 정보를 store에 넣는 메서드

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
        /*
        List<Member> all=findAll();
        for(Member m:all){
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m);
            }
        }
        return Optional.empty();
         */

        return findAll().stream()
                .filter(m->m.getLoginId().equals(loginId))
                .findFirst();
        // 위의 코드를 줄인 형태. Member 내의 데이터 중에서 loginId와 같은 값을 가진 데이터를 만나면 반환한다.
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
