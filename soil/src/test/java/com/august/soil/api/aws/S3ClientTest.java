package com.august.soil.api.aws;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class S3ClientTest {
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  private S3Client s3Client;
  
  @Autowired
  public void setS3Client(S3Client s3Client) {
    this.s3Client = s3Client;
  }
  
  @Test
  @Order(1)
  void S3_버킷으로_이미지_업로드() {
    URL testPhoto = getClass().getResource("/test_profile.jpg");
    assertThat(testPhoto, is(notNullValue()));
    log.info("testPhotoPath: {}", testPhoto.getPath());
    
    File file = new File(testPhoto.getFile());
    String url = s3Client.upload(file);
    assertThat(url, is(notNullValue()));
    log.info("S3 bucket url: {}", url);

//    s3Client.delete(url);
  }
  
  @Test
  @Order(2)
  void S3_버킷_이미지_삭제() {
    URL testPhoto = getClass().getResource("/test_profile.jpg");
    assertThat(testPhoto, is(notNullValue()));
    log.info("testPhotoPath: {}", testPhoto.getPath());
  
    File file = new File(testPhoto.getFile());
    String url = s3Client.upload(file);
    log.info("S3 bucket url: {}", url);
    s3Client.delete(url);
  }
}