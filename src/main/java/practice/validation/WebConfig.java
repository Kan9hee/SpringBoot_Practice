package practice.validation;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import practice.validation.web.filter.LogFilter;
import practice.validation.web.filter.LoginCheckFilter;
import practice.validation.web.interceptor.LoginCheckInterceptor;
import practice.validation.web.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**") // 인터셉터 적용
                .excludePathPatterns("/css/**","/*.ico","/error"); // 인터셉터 미적용

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","members/add","/login","/logout","/css/**","/*/ico","/error");
    }

    //@Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 사용할 필터
        filterRegistrationBean.setOrder(1); // 동작 우선순위
        filterRegistrationBean.addUrlPatterns("/*"); // 필터 적용할 URL패턴

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); //logFilter 다음으로 실행됨
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
