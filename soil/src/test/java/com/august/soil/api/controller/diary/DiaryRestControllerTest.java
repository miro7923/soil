package com.august.soil.api.controller.diary;

import com.august.soil.api.security.WithMockJwtAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiaryRestControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  public void setMockMvc(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("글 목록 조회 테스트")
  void getList() throws Exception {
    Map<String, String> testInput = new HashMap<>();
    testInput.put("category_id", "1");
    testInput.put("title", "test title");
    testInput.put("content", "test content");
    testInput.put("price", "1000");

    ResultActions result = mockMvc.perform(
      get("/api/diaries/list")
    );
    result.andDo(print())
      .andExpect(status().isOk())
      .andExpect(handler().handlerType(DiaryRestController.class))
      .andExpect(handler().methodName("diaries"))
      .andExpect(jsonPath("$.success", is(true)));
  }

  @Test
  @Order(1)
  @WithMockJwtAuthentication
  @DisplayName("사진과 함께 글 작성, 삭제 테스트")
//  @Transactional
  void upload() throws Exception {
    CreateDiaryRequest param = new CreateDiaryRequest();
    param.setTitle("file upload test title 1");
    param.setContent("file upload test content 1");
    param.setCategoryId(1L);
    param.setPrice("1000");

    String testPhotoName = "test_profile.jpg";
    URL testPhoto = getClass().getResource("/test_profile.jpg");
    assertThat(testPhoto, is(notNullValue()));
    log.info("testPhotoPath: {}", testPhoto.getPath());

    MockMultipartFile file = new MockMultipartFile(
      "file",
      testPhotoName,
      MediaType.IMAGE_JPEG_VALUE,
      "<<jpeg data>>".getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart("/api/diaries/upload")
          .file(file)
          .param("categoryId", "1")
          .param("title", "file upload test title 1")
          .param("content", "file upload test content 1")
          .param("price", "1000")
          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
      ).andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @WithMockJwtAuthentication
  @DisplayName("사진 포함된 일기 삭제")
  void deleteDiary() throws Exception {
    mockMvc.perform(
        delete("/api/diaries/48")
      ).andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("쿼리 파라미터로 검색")
  void search() throws Exception {
    mockMvc.perform(
        get("/api/diaries/search?keyword=test")
      ).andDo(print())
      .andExpect(status().isOk())
      .andExpect(handler().handlerType(DiaryRestController.class))
      .andExpect(handler().methodName("search"))
      .andExpect(jsonPath("$.success", is(true)));
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("글 수정 테스트")
  void updateDiary() throws Exception {
    Long categoryId = 1L;
    String title = "글 수정 테스트 제목";
    String content = "수정 테스트 본문";
    String price = "20000";

    UpdateDiaryRequest request = new UpdateDiaryRequest();
    request.setCategoryId(categoryId);
    request.setTitle(title);
    request.setContent(content);
    request.setPrice(price);

    String body = mapper.writeValueAsString(request);

    mockMvc.perform(
        patch("/api/diaries/5")
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andDo(print())
      .andExpect(status().isOk());
  }
}