package com.august.soil.api.service.user;

import com.august.soil.api.error.NotFoundException;
import com.august.soil.api.error.UnauthorizedException;
import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.user.Email;
import com.august.soil.api.model.user.User;
import com.august.soil.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
    
  public User login(Email email, String resultcode) {
    // 사용자 정보 API 접근 성공시 응답 샘플
    // "resultcode": "00",
    //  "message": "success"
    // TODO : 회원 인증 정보로 비밀번호 대신 네이버 회원정보 API 응답 결과 코드를 이용함
    // 회원정보 API 응답 성공시 받는 "00" 외에는 다 로그인 거절 처리 예정
    checkArgument(resultcode != null, "resultcode must be provided.");
  
    if (!resultcode.equals("00")) {
      throw new UnauthorizedException("Unexpected resultcode.");
    }
    
    return findByEmail(email)
             .orElseThrow(() -> new NotFoundException(User.class, email));
  }

  /**
   * 회원 등록
   * 중복 회원 검사 후 통과하면 다음 트랜잭션 진행
   * @param user
   * @return member_id
   */
  @Transactional
  public User join(User user) {
    validateDuplicatedMember(user);
    return userRepository.save(user);
  }

  /**
   * 중복 회원 검사
   * @param user
   */
  private void validateDuplicatedMember(User user) {
    Optional<User> findMember = userRepository.findByEmail(user.getEmail());
    if (!findMember.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  /**
   * id로 회원 한 명 조회
   * @param id
   * @return Optional<User>
   */
  public Optional<User> findById(Id<User, Long> id) {
        return userRepository.findById(id);
    }
  
  /**
   * Email로 회원 한 명 조회
   * @param email
   * @return Optional<User>
   */
  public Optional<User> findByEmail(Email email) {
    checkArgument(email != null, "email must be provided.");
  
    return userRepository.findByEmail(email);
  }

  /**
   * 회원 전체 조회
   * @return List<User>
   */
  public List<User> findUsers() {
        return userRepository.findAll();
    }
  
  /**
   * 회원 정보를 업데이트하는 메서드로 소셜 로그인을 사용하기 때문에 email은 불변일 것으로 보여 이름만 수정 가능하게 함(개명했을 경우)
   * @param id, newName
   * @return
   */
  @Transactional
  public void update(Id<User, Long> id, String newName) {
    Optional<User> user = userRepository.findById(id);
    user.ifPresent(value -> value.setName(newName));
  }
}
