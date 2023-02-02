package com.august.soil.api.repository.user;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

  private final EntityManager em;
  
  /**
   * 사용자 정보를 저장하는 메서드
   * @param user 사용자 정보를 담은 객체
   * @return User 저장된 사용자 객체
   * 해당 메서드는 테이블 데이터를 변경시키는 메서드이기 때문에 트랜잭션 readonly false(기본값)로 설정한다.
   */
  @Override
  public User save(User user) {
    em.persist(user);
    return user;
  }
  
  /**
   * 테이블에 저장된 모든 회원 정보를 가져오는 메서드
   * @return List<User> 회원 목록
   */
  @Override
  public List<User> findAll() {
    return em.createQuery("select u from User u", User.class)
      .getResultList();
  }
  
  /**
   * PK로 회원 정보를 select 해 오는 메서드
   * @param id 찾고자 하는 회원 PK
   * @return Optional<User> select 결과를 담은 Optional 객체
   */
  @Override
  public Optional<User> findById(Id<User, Long> id) {
    return ofNullable(em.createQuery("select u from User u where u.id = :id", User.class)
      .setParameter("id", id.getValue())
      .getSingleResult());
  }
  
  /**
   * 이메일 주소로 회원 정보를 select 해 오는 메서드
   * @param email 찾고자 하는 회원의 이메일
   * @return Optional<User> select 결과를 담은 Optional 객체
   */
  @Override
  public Optional<User> findByEmail(Email email) {
    List<User> resultList = em.createQuery("select u from User u where u.email = :email", User.class)
                          .setParameter("email", email)
                          .getResultList();
    
    if (resultList.isEmpty()) return Optional.empty();
    
    return ofNullable(resultList.get(0));
  }

  /**
   * 회원 아이디로 db에서 회원정보 삭제
   * @param id 탈퇴시키고자 하는 회원의 id정보를 담은 객체
   * @return 성공시 true, 실패시 false 리턴
   */
  @Override
  public boolean deleteById(Id<User, Long> id) {
    User user = em.find(User.class, id.getValue());

    if (user == null) {
      // db에 회원정보가 존재하지 않으면 false
      return false;
    }

    // 삭제에 성공하면 true
    em.remove(user);
    return true;
  }
}
