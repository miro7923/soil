package com.august.soil.api.repository.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class JpaCategoryRepository implements CategoryRepository {

  private final EntityManager em;
  
  /**
   * 새로 생성된 카테고리 정보를 저장하는 메서드
   * @param category 카테고리 객체
   * @return Category 인자값으로 받은 카테고리 객체
   */
  @Override
  public Category save(Category category) {
    em.persist(category);
    return category;
  }

  /**
   * PK로 카테고리 하나를 select 해오는 메서드
   * @param id 찾고자 하는 카테고리의 PK
   * @return Optional<Category> null값에 대한 예외처리를 편하게 하기 위해 Optional로 감싸서 객체를 리턴함
   * (찾고자 하는 카테고리가 없을 경우 대비)
   * 리턴받은 쪽에서는 category.isPresent() 등의 메서드를 사용해서 리턴된 데이터가 있는지(null인지 아닌지) 확인 후 사용하면 된다.
   * ofNullable-> 자동 null값 (미)출력 처리
   */
  @Override
  public Optional<Category> findById(Id<Category, Long> id) {
    return ofNullable(
      em.find(Category.class, id.getValue())
    );
  }

  /**
   * 테이블에 저장된 모든 카테고리 정보를 select 해오는 메서드
   * @param id 사용자의 id
   * @return List<Category> 카테고리 객체를 담은 리스트
   */

  @Override
  public List<Category> findAll(Id<User, Long> id) {
    return em.createQuery(
      "select c " +
        "from Category c " +
        "where c.user.id = :id", Category.class)
      .setParameter("id", id.getValue())
      .getResultList();
  }

  /**
   * 카테고리명으로 카테고리 정보를 select 해오는 메서드
   * @param id 카테고리의 소유자인 회원 id
   * @param name 카테고리명
   * @return List<Category> 카테고리 객체를 담은 리스트
   */
  @Override
  public List<Category> findByName(Id<User, Long> id, String name) {
    return em.createQuery(
      "select c " +
        "from Category c " +
        "where c.user.id = :id " +
        "and c.name = :name", Category.class)
      .setParameter("id", id.getValue())
      .setParameter("name", name)
      .getResultList();
  }

  /**
   * 카테코리 하나 삭제
   * @param id 삭제하고자 하는 일기의 PK
   * @return 성공하면 true, 실패하면 false
   */
  @Override
  public boolean deleteById(Id<Category, Long> id) {
    Category category = em.find(Category.class, id.getValue());

    if(id == null) {
      // 해당하는 카테고리가 없으면 false를 반환
      return false;
    }

    em.remove(category);
    return true;
  }
}