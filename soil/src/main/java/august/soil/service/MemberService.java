package august.soil.service;

import august.soil.domain.Member;
import august.soil.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 등록
     * 중복 회원 검사 후 통과하면 다음 트랜잭션 진행
     * @param member
     * @return member_id
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검사
     * @param member
     */
    private void validateDuplicatedMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * id로 회원 한 명 조회
     * @param id
     * @return member_id
     */
    public Member findMember(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * name으로 회원 한 명 조회
     * @param name
     * @return List<Member>
     */
    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    /**
     * 회원 전체 조회
     * @return List<Member>
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
