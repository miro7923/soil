package august.soil;

import august.soil.web.interceptor.CorsInterceptor;
import august.soil.web.interceptor.LogInterceptor;
import august.soil.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor())
                .order(1);

        registry.addInterceptor(new LogInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members", "/login", "/logout", "/css/**", "/*.icd", "/error");
    }
}
