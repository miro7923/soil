package com.august.soil.api.service.diary;

import com.august.soil.api.controller.diary.UpdateDiaryRequest;
import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import com.august.soil.api.repository.category.CategoryRepository;
import com.august.soil.api.repository.category.JpaCategoryRepository;
import com.august.soil.api.repository.diary.DiaryRepository;
import com.august.soil.api.repository.diary.JpaDiaryRepository;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// 기본은 readonly true로 설정하되, save 메서드와 같이 트랜젝션 변경이 필요한 메서드에 대해서만 readonly false로 설정한다.
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

  // 변경할 일 없으니까 final
  private final DiaryRepository diaryRepository;
  private final CategoryRepository categoryRepository;

  /**
   * 일기 등록
   * @param diary
   * @return diary_id
   */
  @Transactional
  public Diary upload(Diary diary) {
    return diaryRepository.save(diary);
  }

  /**
   * 일기 전체 조회
   * @return List<Diary>
   */
  public List<Diary> findDiaries(Id<User, Long> id, int offset, int limit) {
    Preconditions.checkArgument(id != null, "id must be provided.");
    
    return diaryRepository.findAll(id, checkOffset(offset), checkLimit(limit));
  }
  
  private int checkOffset(int offset) {
    return offset < 0 ? 0 : offset;
  }
  
  private int checkLimit(int limit) {
    return (limit < 1 || limit > 10) ? 10 : limit;
  }

  /**
   * 일기 하나 조회
   * @param id
   * @return Diary
   */
  public Optional<Diary> findDiary(Id<Diary, Long> id) {
        return diaryRepository.findById(id);
    }

  /**
   * 일기 수정
   * @param id
   * @param // 수정할 정보를 담은 request 객체
   */
  @Transactional
  public boolean updateDiary(Id<Diary, Long> id, UpdateDiaryRequest request) {
    Optional<Diary> findDiary = diaryRepository.findById(id);
    if (findDiary.isEmpty()) {
      // 일기 정보 없음
      return false;
    }
  
    Optional<Category> category = categoryRepository.findById(Id.of(Category.class, request.getCategory_id()));
    Diary diary = findDiary.get();
    diary.setCategory(category.get());
    diary.setTitle(request.getTitle());
    diary.setContent(request.getContent());
    diary.setPrice(request.getPrice());
    // 일기 정보가 있고 수정 성공
    return true;
  }

  @Transactional
  public boolean deleteDiary(Id<Diary, Long> id) {
        return diaryRepository.deleteById(id);
    }
}
