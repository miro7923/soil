package com.august.soil.api.controller.diary;

import com.august.soil.api.SoilApplication;
import com.august.soil.api.security.WithMockJwtAuthentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiaryRestControllerTest {
  
  private MockMvc mockMvc;
  
  private Logger log = LoggerFactory.getLogger(getClass());
  
  @Autowired
  ObjectMapper objectMapper;
  
  @Autowired
  public void setMockMvc(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }
  
  @Test
  @WithMockJwtAuthentication
  @DisplayName("??? ?????? ?????? ?????????")
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
  @DisplayName("????????? ?????? ??? ??????, ?????? ?????????")
//  @Transactional
  void upload() throws Exception {
    CreateDiaryRequest param = new CreateDiaryRequest();
    param.setTitle("file upload test title 1");
    param.setContent("file upload test content 1");
    param.setCategory_id(1L);
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
          .param("category_id", "1")
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
  @DisplayName("?????? ????????? ?????? ??????")
  void deleteDiary() throws Exception {
    mockMvc.perform(
        delete("/api/diaries/34")
      ).andDo(print())
      .andExpect(status().isOk());
  }
}