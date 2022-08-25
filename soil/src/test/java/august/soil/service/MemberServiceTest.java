package august.soil.service;

import august.soil.domain.Member;
import august.soil.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member("memberC");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복회원검증() throws Exception {
        // given
        Member member = new Member("memberB");
        Member member1 = new Member("memberB");

        // when
        memberService.join(member);
        try {
            memberService.join(member1); // 예외 발생해야 함
        } catch (IllegalStateException e) {
            return;
        }

        // then
        fail("예외 발생해야 함");
    }
}