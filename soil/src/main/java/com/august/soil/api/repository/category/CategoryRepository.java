package com.august.soil.api.repository.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
  
  Category save(Category category);
  
  Optional<Category> findById(Id<Category, Long> id);
  
  List<Category> findAll();
  
  List<Category> findByName(String name);
}
