package august.soil.repository;

import august.soil.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepository {

    private final EntityManager em;

    public void save(Diary diary) {
        em.persist(diary);
    }

    public Diary findOne(Long id) {
        return em.find(Diary.class, id);
    }

    public List<Diary> findAll() {
        return em.createQuery("select d from Diary d", Diary.class)
                .getResultList();
    }

    // 일기 주제(상품)으로 찾는 메서드
    public List<Diary> findByTitle(String title) {
        return em.createQuery("select d from Diary d where d.title = :title", Diary.class)
                .setParameter("title", title)
                .getResultList();
    }
}
