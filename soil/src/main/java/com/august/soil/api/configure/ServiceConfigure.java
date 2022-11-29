package com.august.soil.api.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.august.soil.api.aws.S3Client;
import com.august.soil.api.security.Jwt;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service layer의 설정 빈 등록
 */
@Configuration
public class ServiceConfigure {
  
  @Bean
  public Jwt jwt(JwtTokenConfigure jwtTokenConfigure) {
    return new Jwt(jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret(), jwtTokenConfigure.getExpirySeconds());
  }
  
  @Bean
  public StringEncryptor jasyptStringEncryptor(JasyptConfigure jasyptConfigure) {
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(jasyptConfigure.getPassword());
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations(1000);
    config.setPoolSize(1);
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);
    return encryptor;
  }
  
  @Bean
  public AmazonS3 amazonS3Client(AwsConfigure awsConfigure) {
    return AmazonS3ClientBuilder.standard()
      .withRegion(Regions.fromName(awsConfigure.getRegion()))
      .withCredentials(
        new AWSStaticCredentialsProvider(
          new BasicAWSCredentials(
            awsConfigure.getAccessKey(),
            awsConfigure.getSecretKey())
          )
        )
      .build();
  }
  
  @Bean
  public S3Client s3Client(AmazonS3 amazonS3, AwsConfigure awsConfigure) {
    return new S3Client(amazonS3, awsConfigure.getUrl(), awsConfigure.getBucketName());
  }
  
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builer -> {
      AfterburnerModule abm = new AfterburnerModule();
      JavaTimeModule jtm = new JavaTimeModule();
      jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
  
      builer.visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
      builer.visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
      builer.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      builer.serializationInclusion(JsonInclude.Include.NON_NULL);
      builer.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      builer.modulesToInstall(abm, jtm);
    };
  }
}
