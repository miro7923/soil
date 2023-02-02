package com.august.soil.api.service.diary;

import com.august.soil.api.model.commons.Id;
import com.august.soil.api.model.diary.Diary;
import com.august.soil.api.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiaryServiceTest {
  
  @Autowired
  private DiaryService diaryService;
  
  @Test
  @DisplayName("검색 테스트")
  void search() {
    List<Diary> testList = diaryService.findDiaries(
      Id.of(User.class, 2L),
      "확인",
      0,
      10
    );
    assertThat(testList, is(notNullValue()));
    assertThat(testList.size(), is(7));
    log.info("test list len: {}", testList.size());
  }
}