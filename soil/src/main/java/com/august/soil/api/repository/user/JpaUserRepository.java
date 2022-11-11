package com.august.soil.api.repository.user;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

  private final EntityManager em;

  @Transactional
  public User save(User user) {
    log.info("@@@@@@@ member: "+user);
    em.persist(user);
    return user;
  }

  @Override
  public List<User> findAll() {
    return em.createQuery("select u from User u", User.class)
      .getResultList();
  }

  @Override
  public Optional<User> findById(Id<User, Long> id) {
    return ofNullable(em.createQuery("select u from User u where u.id = :id", User.class)
      .setParameter("id", id.getValue())
      .getSingleResult());
  }
  
  @Override
  public Optional<User> findByEmail(Email email) {
    List<User> resultList = em.createQuery("select u from User u where u.email = :email", User.class)
                          .setParameter("email", email)
                          .getResultList();
    
    if (resultList.isEmpty()) return Optional.empty();
    
    return ofNullable(resultList.get(0));
  }
}
