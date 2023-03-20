package practice.web;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import practice.web.converter.IntegerToStringConverter;
import practice.web.converter.IpPortToStringConverter;
import practice.web.converter.StringToIntegerConverter;
import practice.web.converter.StringToIpPortConverter;
import practice.web.formatter.MyNumberFormatter;
import practice.web.resolver.argumentresolver.LoginMemberArgumentResolver;
import practice.web.filter.LogFilter;
import practice.web.filter.LoginCheckFilter;
import practice.web.interceptor.LoginInterceptor;
import practice.web.resolver.MyHandlerExceptionResolver;
import practice.web.resolver.UserHandlerExceptionResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error","/error-page/**"); // 오류 페이지 경로
        // dispatcherPath를 쓰지 못하는 대신, excludePathPatterns을 사용한다.
        // 이 경우, 인터셉터는 경로의 중복 호출을 제거하여 WAS의 오류 반환시 필터와 인터셉터의 호출을 생략한다.
    }

    @Override
    public void addFormatters(FormatterRegistry registry){
        //registry.addConverter(new StringToIntegerConverter());
        //registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
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

    //@Bean
    public FilterRegistrationBean logFilterV2(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        //filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.ERROR);
        // 오류페이지 요청 전용 필터 사용시 DispatcherType.ERROR를 사용한다.

        return filterRegistrationBean;
    }
}
