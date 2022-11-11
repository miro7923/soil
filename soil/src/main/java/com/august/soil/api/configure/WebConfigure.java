package com.august.soil.api.configure;

import com.august.soil.api.web.interceptor.CorsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfigure implements WebMvcConfigurer {

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
