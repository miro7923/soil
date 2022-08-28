package august.soil.repository;

import august.soil.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByName(String loginId) {
        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }
}
