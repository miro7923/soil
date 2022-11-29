package com.august.soil.api.model.commons;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.*;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.*;

@RequiredArgsConstructor
public class AttachedFile {
  
  private final String originalFileName;
  
  private final String contentType;
  
  private final byte[] bytes;
  
  private static boolean verify(MultipartFile file) {
    if (file != null && file.getSize() > 0 && file.getOriginalFilename() != null) {
      String contentType = file.getContentType();
      // 첨부파일 타입을 확인하고 이미지인 경우 처리
      return isNotEmpty(contentType) && contentType.toLowerCase().startsWith("image");
    }
    return false;
  }
  
  public static Optional<AttachedFile> toAttachedFile(MultipartFile file) {
    try {
      if (verify(file)) {
        return of(
          new AttachedFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())
        );
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return empty();
  }
  
  public String extension(String defaultExtension) {
    return defaultIfEmpty(getExtension(originalFileName), defaultExtension);
  }
  
  public String randomName(String defaultExtension) {
    return randomName(null, defaultExtension);
  }
  
  public String randomName(String basePath, String defaultExtension) {
    String name = isEmpty(basePath) ? UUID.randomUUID().toString() : basePath + "/" + UUID.randomUUID().toString();
    return name + "." + extension(defaultExtension);
  }
  
  public InputStream inputStream() {
    return new ByteArrayInputStream(bytes);
  }
  
  public long length() {
    return bytes.length;
  }
  
  public String getContentType() {
    return contentType;
  }
}
