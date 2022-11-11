package com.august.soil.api.repository.diary;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaDiaryRepository implements DiaryRepository {

  private final EntityManager em;

  public Diary save(Diary diary) {
    em.persist(diary);
    return diary;
  }

  public Optional<Diary> findById(Id<Diary, Long> id) {
    return Optional.ofNullable(
      em.find(Diary.class, id.getValue())
    );
  }
  
  /**
   * 회원 한 명이 작성한 모든 일기 목록 조회
   * @param id
   * @return List<Diary>
   */
  public List<Diary> findAll(Id<User, Long> id) {
    return em.createQuery("select d from Diary d where d.user.id = :user_id", Diary.class)
      .setParameter("user_id", id.getValue())
      .getResultList();
  }

  /**
   * 일기 주제(상품)로 일기 목록을 찾는 메서드
   * @param title
   * @return 주제에 해당하는 일기 목록
   */
  public List<Diary> findByTitle(String title) {
    return em.createQuery("select d from Diary d where d.title = :title", Diary.class)
      .setParameter("title", title)
      .getResultList();
  }

  /**
   * 일기 하나 삭제
   * @param id
   * @return 성공하면 true, 실패하면 false
   */
  public boolean deleteById(Id<Diary, Long> id) {
    Diary diary = em.find(Diary.class, id.getValue());

    if (diary == null) {
      // 해당하는 일기가 없으면 false 반환
      return false;
    }

    em.remove(diary);
    return true;
  }
}
