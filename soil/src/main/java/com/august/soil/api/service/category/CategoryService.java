package com.august.soil.api.service.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 추가
     * 중복 카테고리 검사 후 통과하면 다음 트랜잭션 진행
     * @param category
     * @return category_id
     */
    @Transactional
    public Long add(Category category) {
        validateDuplicatedCategory(category.getName());
        categoryRepository.save(category);
        return category.getId();
    }

    /**
     * 중복 카테고리 검사
     * @param name
     */
    private void validateDuplicatedCategory(String name) {
        List<Category> findMembers = categoryRepository.findByName(name);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다.");
        }
    }

    /**
     * id로 카테고리 검색
     * @param id
     * @return Category
     */
    public Category findCategory(Long id) {
        return categoryRepository.findOne(id);
    }

    /**
     * name으로 카테고리 검색
     * @param name
     * @return List<Category>
     */
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * 카테고리 전체 조회
     * @return List<Category>
     */
    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }
}
