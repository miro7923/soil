package com.august.soil.api.repository.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
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
   * PK로 카테고리 하나를 select 해 오는 메서드
   * @param id 찾고자 하는 카테고리의 PK
   * @return Optional<Category> null값에 대한 예외처리를 편하게 하기 위해 Optional로 감싸서 객체를 리턴함
   * 리턴받은 쪽에서는 category.isPresent() 등의 메서드를 사용해서 리턴된 데이터가 있는지(null인지 아닌지) 확인 후 사용하면 된다.
   */
  @Override
  public Optional<Category> findById(Id<Category, Long> id) {
    return ofNullable(
      em.find(Category.class, id.getValue())
    );
  }
  
  /**
   * 테이블에 저장된 모든 카테고리 정보를 select 해 오는 메서드
   * @return List<Category> 카테고리 객체를 담은 리스트
   */
  @Override
  public List<Category> findAll() {
    return em.createQuery("select c from Category c", Category.class)
      .getResultList();
  }
  
  /**
   * 카테고리명으로 카테고리 정보를 select 해 오는 메서드
   * @param name 카테고리명
   * @return List<Category> 카테고리 객체를 담은 리스트
   */
  @Override
  public List<Category> findByName(String name) {
    return em.createQuery("select c from Category c where c.name = :name", Category.class)
      .setParameter("name", name)
      .getResultList();
  }
}
