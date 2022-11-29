package com.august.soil.api.configure;

import com.august.soil.api.configure.support.SimpleOffsetPageableHandlerMethodArgumentResolver;
import com.august.soil.api.web.interceptor.CorsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvc 설정정보를 담은 클래스로 CORS 정책을 허용하는 용도 및 ArgumentResolver 추가용
 */
@Slf4j
@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
  
  @Bean
  public SimpleOffsetPageableHandlerMethodArgumentResolver simpleOffsetPageableHandlerMethodArgumentResolver() {
    return new SimpleOffsetPageableHandlerMethodArgumentResolver();
  }
  
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(simpleOffsetPageableHandlerMethodArgumentResolver());
  }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }

//    @Bean
//    public CookieSerializer cookieSerializer() throws MalformedURLException {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setCookieName("loginMember");
//        serializer.setSameSite("None");
//        return serializer;
//    }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new CorsInterceptor())
            .order(1)
            .addPathPatterns("/**");

//        registry.addInterceptor(new LogInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/*.ico", "/error");
//
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(3)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/members", "/login", "/logout", "/css/**", "/*.icd", "/error");
    }
}
