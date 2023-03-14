package practice.validation.servlet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("exception appeared!");
    }
    // 에러코드 500 - 서버 내에서 처리할 수 없는 예외 발생

    @GetMapping("error-404")
    public void error404(HttpServletResponse response)throws IOException{
        response.sendError(404,"error code 404");
    }
    @GetMapping("error-500")
    public void error500(HttpServletResponse response)throws IOException{
        response.sendError(500,"error code 500");
    }
    // 서블릿의 sendError를 사용해 오류 발생 여부를 서블릿 컨테이너에 전달한다. 상태코드와 오류메시지를 포함한다.
}
