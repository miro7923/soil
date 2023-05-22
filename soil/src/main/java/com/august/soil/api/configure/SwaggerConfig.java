package com.august.soil.api.configure;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .groupName("august")
      .apiInfo(this.apiInfo())
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.august.soil.api.controller"))
      .paths(PathSelectors.ant("/api/**"))
      .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title("Soil")
      .description("Api list")
      .build();
  }
}
