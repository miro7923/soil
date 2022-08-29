package august.soil.service;

import august.soil.domain.Member;
import august.soil.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * id로 회원 한 명 조회
     * @param id
     * @return 회원의 DB 인덱스
     */
    public Member findMember(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * loginId로 회원 조회
     * @param loginId
     * @return 해당하는 회원
     */
    public Optional<Member> findByName(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    /**
     * 회원 전체 조회
     * @return 가입된 회원 전체 목록
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void update(Long id, String password) {
        Member member = memberRepository.findOne(id);
        member.setPassword(password);
    }
}
