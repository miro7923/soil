package com.august.soil.api.service.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.repository.category.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// 기본은 readonly true로 설정하되, save 메서드와 같이 트랜젝션 변경이 필요한 메서드에 대해서만 readonly false로 설정한다.
@Transactional(readOnly = true)
public class CategoryService {

  private final JpaCategoryRepository categoryRepository;

  /**
   * 카테고리 추가 메서드
   * 중복 카테고리 검사 후 통과하면 다음 트랜잭션 진행
   * @param category 추가하려는 카테고리 객체
   * @return 추가된 카테고리 객체
   */
  @Transactional
  public Category add(Category category) {
    validateDuplicatedCategory(category.getName());
    categoryRepository.save(category);
    return category;
  }

  /**
   * 중복 카테고리 검사
   * @param name 중복검사를 시행하려는 카테고리명
   */
  private void validateDuplicatedCategory(String name) {
    List<Category> findMembers = categoryRepository.findByName(name);
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 카테고리입니다.");
    }
  }

  /**
   * PK로 카테고리 검색
   * @param id 찾고자 하는 카테고리 PK
   * @return Optional로 감싼 카테고리 객체
   */
  public Optional<Category> findCategory(Id<Category, Long> id) {
        return categoryRepository.findById(id);
    }

  /**
   * name으로 카테고리 검색
   * @param name 찾고자 하는 카테고리명
   * @return 찾은 카테고리 목록
   */
  public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

  /**
   * 카테고리 전체 조회
   * @return 전체 카테고리 목록
   */
  public List<Category> findCategories() {
        return categoryRepository.findAll();
    }
}
