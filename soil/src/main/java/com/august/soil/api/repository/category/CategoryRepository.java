package com.august.soil.api.repository.category;

import com.august.soil.api.model.category.Category;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
  
  Category save(Category category);

  Optional<Category> findById(Id<Category, Long> id);

  List<Category> findAll(Id<User, Long> id);

  List<Category> findByName(Id<User, Long> id, String name);

  boolean deleteById(Id<Category, Long> id);

}