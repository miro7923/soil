package com.august.soil.api.controller.category;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  @WithMockJwtAuthentication
  @DisplayName("카테고리 생성 테스트")
  @Order(1)
//  @Transactional
  void createCategory() throws Exception {
    String name = "테스트 카테고리2";

    CreateCategoryRequest request = new CreateCategoryRequest();
    request.setName(name);

    String body = mapper.writeValueAsString(request);

    mockMvc.perform(
        post("/api/category/add")
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("카테고리 이름 수정 테스트")
  @Order(2)
  void updateCategory() throws Exception {
    String name = "수정 카테고리";

    UpdateCategoryRequest request = new UpdateCategoryRequest();
    request.setNewName(name);
    request.setOriginName("테스트 카테고리");

    String body = mapper.writeValueAsString(request);

    mockMvc.perform(
        patch("/api/category/update")
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("카테고리 삭제 테스트")
  @Order(3)
  @Transactional
  void removeCategory() throws Exception {
    String name = "수정 카테고리";

    RemoveCategoryRequest request = new RemoveCategoryRequest();
    request.setName(name);

    String body = mapper.writeValueAsString(request);

    mockMvc.perform(
        delete("/api/category/remove")
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @WithMockJwtAuthentication
  @DisplayName("카테고리 목록 조회 테스트")
  @Order(4)
  void selectList() throws Exception {
    mockMvc.perform(
        get("/api/category/categoryList")
      ).andDo(print())
      .andExpect(status().isOk());
  }
}