package com.august.soil.api.repository.user;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    User save(User member);

    List<User> findAll();

    Optional<User> findById(Id<User, Long> id);
    
    Optional<User> findByEmail(Email email);
}
