package august.soil;

import august.soil.web.interceptor.CorsInterceptor;
import august.soil.web.interceptor.LogInterceptor;
import august.soil.web.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.MalformedURLException;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        log.info("call addCorsMappings");
//        registry.addMapping("/**")
//                .allowedOrigins("http://127.0.0.1:54422")
//                .allowedMethods("*")
//                .allowedHeaders("*");
//    }

    @Bean
    public CookieSerializer cookieSerializer() throws MalformedURLException {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("loginMember");
        serializer.setSameSite("None");
        return serializer;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor())
                .addPathPatterns("/**")
                .order(1);

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
