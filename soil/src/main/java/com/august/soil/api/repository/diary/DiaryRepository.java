package com.august.soil.api.repository.diary;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {
  
  Diary save(Diary diary);
  
  Optional<Diary> findById(Id<Diary, Long> id);
  
  List<Diary> findAll(Id<User, Long> id);
  
  List<Diary> findByTitle(String title);
  
  boolean deleteById(Id<Diary, Long> id);
}
