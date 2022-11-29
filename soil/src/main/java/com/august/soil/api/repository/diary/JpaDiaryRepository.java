package com.august.soil.api.repository.diary;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class JpaDiaryRepository implements DiaryRepository {

  private final EntityManager em;
  
  /**
   * 일기 정보를 테이블에 저장하는 메서드
   * @param diary 일기 객체
   * @return Diary 저장된 일기 객체
   */
  @Override
  public Diary save(Diary diary) {
    em.persist(diary);
    return diary;
  }
  
  /**
   * PK로 일기 데이터를 select 해 오는 메서드
   * @param id 찾고자 하는 일기의 PK
   * @return Optional<Diary> Optional로 감싼 객체를 리턴한다. null값에 대한 예외처리를 보다 간단한 코드로 처리할 수 있다.
   */
  @Override
  public Optional<Diary> findById(Id<Diary, Long> id) {
    return ofNullable(
      em.find(Diary.class, id.getValue())
    );
  }
  
  /**
   * 사용자 한 명이 작성한 모든 일기 목록 조회
   * @param id 사용자 PK
   * @param offset 페이지 번호(페이징 offset)
   * @param limit 조회할 최대 갯수
   * @return List<Diary> 사용자 PK로 찾은 일기 리스트
   */
  @Override
  public List<Diary> findAll(Id<User, Long> id, int offset, int limit) {
    return em.createQuery("SELECT d " +
                            "FROM Diary d " +
                            "WHERE d.user.id = :user_id " +
                            "ORDER BY d.createAt DESC", Diary.class)
      .setParameter("user_id", id.getValue())
      .setFirstResult(offset)
      .setMaxResults(limit)
      .getResultList();
  }

  /**
   * 일기 주제(상품)로 일기 목록을 찾는 메서드
   * @param title 일기 주제(상품명)
   * @return List<Diary> 주제에 해당하는 일기 목록
   */
  @Override
  public List<Diary> findByTitle(String title) {
    return em.createQuery("SELECT d " +
                            "FROM Diary d " +
                            "WHERE d.title = :title " +
                            "ORDER BY d.createAt DESC", Diary.class)
      .setParameter("title", title)
      .getResultList();
  }

  /**
   * 일기 하나 삭제
   * @param id 삭제하고자 하는 일기의 PK
   * @return 성공하면 true, 실패하면 false
   */
  @Override
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
