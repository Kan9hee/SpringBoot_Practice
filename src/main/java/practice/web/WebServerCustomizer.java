package practice.web;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory){

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error-404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.NOT_FOUND,"/error-500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class,"/error-500");
        // RuntimeException의 자식 예외도 함께 처리한다.

        factory.addErrorPages(errorPage404,errorPage500,errorPageEx);
    }
}
