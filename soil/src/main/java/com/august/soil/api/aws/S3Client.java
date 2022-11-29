package com.august.soil.api.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequiredArgsConstructor
public final class S3Client {
  
  private final AmazonS3 amazonS3;
  
  private final String url;
  
  private final String bucketName;
  
  public S3Object get(String key) {
    GetObjectRequest request = new GetObjectRequest(bucketName, key);
    return amazonS3.getObject(request);
  }
  
  public String upload(File file) {
    PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
    return executePut(request);
  }
  
  public String upload(byte[] bytes, String basePath, Map<String, String> metadata) {
    String name = isEmpty(basePath) ? UUID.randomUUID().toString() : basePath + "/" + UUID.randomUUID().toString();
    return upload(new ByteArrayInputStream(bytes), bytes.length, name + ".jpeg", "image/jpeg", metadata);
  }
  
  public String upload(InputStream in, long length, String key, String contentType, Map<String, String> metadata) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    if (contentType != null) {
      objectMetadata.addUserMetadata("Content-Type", contentType);
      objectMetadata.setContentLength(length);
    }
  
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, in, objectMetadata);
    putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
    PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
    return url + "/" + bucketName + "/" + key;
  }
  
  public void delete(String url) {
    String key = getName(url);
    DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);
    executeDelete(request);
  }
  
  private void executeDelete(DeleteObjectRequest request) {
    amazonS3.deleteObject(request);
  }
  
  private String executePut(PutObjectRequest request) {
    amazonS3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));
    StringBuilder sb = new StringBuilder(url);
    if (!url.endsWith("/"))
      sb.append("/");
    sb.append(bucketName);
    sb.append("/");
    sb.append(request.getKey());
    return sb.toString();
  }
}
