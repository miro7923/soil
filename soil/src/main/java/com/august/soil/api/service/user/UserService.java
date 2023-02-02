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
// 기본은 readonly true로 설정하되, save 메서드와 같이 트랜젝션 변경이 필요한 메서드에 대해서만 readonly false로 설정한다.
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  
  /**
   * 사용자 로그인 처리
   * @param email 로그인 할 사용자 이메일
   * @param resultcode 네이버 로그인 API에 사용자 정보 요청 결과로 받은 응답 코드
   * @return 로그인 된 사용자 객체
   */
  public User login(Email email, String resultcode) {
    /**
     * 사용자 정보 API 접근 성공시 응답 샘플
     * "resultcode": "00",
     * "message": "success"
     * 회원정보 API 응답 성공시 받는 "00" 외에는 다 로그인 거절 처리
     */
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
   * @param user 가입처리 할 사용자 객체
   * @return 가입된 사용자 객체
   */
  @Transactional
  public User join(User user) {
    validateDuplicatedMember(user);
    return userRepository.save(user);
  }

  /**
   * 중복 회원 검사
   * @param user 중복검사를 진행할 사용자 객체
   */
  private void validateDuplicatedMember(User user) {
    Optional<User> findMember = userRepository.findByEmail(user.getEmail());
    if (!findMember.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  /**
   * PK로 회원 한 명 조회
   * @param id 조회할 회원의 PK
   * @return Optional로 감싼 회원 객체
   */
  public Optional<User> findById(Id<User, Long> id) {
        return userRepository.findById(id);
    }
  
  /**
   * Email로 회원 한 명 조회
   * @param email 조회할 회원 이메일
   * @return Optional로 감싼 회원 객체
   */
  public Optional<User> findByEmail(Email email) {
    checkArgument(email != null, "email must be provided.");
  
    return userRepository.findByEmail(email);
  }

  /**
   * 회원 전체 조회
   * @return 전체 회원 리스트
   */
  public List<User> findUsers() {
        return userRepository.findAll();
    }
  
  /**
   * 회원 정보를 업데이트하는 메서드로 소셜 로그인을 사용하기 때문에 email은 불변일 것으로 보여 이름만 수정 가능하게 함(개명했을 경우)
   * @param id 업데이트 할 회원의 PK
   * @param newName 업데이트 할 이름
   */
  @Transactional
  public void update(Id<User, Long> id, String newName) {
    Optional<User> user = userRepository.findById(id);
    user.ifPresent(value -> value.setName(newName));
  }

  /**
   * db에서 회원정보 삭제(탈퇴처리)
   * @param id 탈퇴하고자 하는 회원의 id 정보를 담은 객체
   * @return 성공시 true, 실패시 false
   */
  @Transactional
  public boolean deleteUser(Id<User, Long> id) {
    return userRepository.deleteById(id);
  }
}
