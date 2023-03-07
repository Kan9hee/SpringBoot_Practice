package practice.validation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.validation.web.controller.form.ItemSaveForm;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemController {
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){
        // ModelAttribute는 필드 단위로 바인딩이 적용되어, 일부분에 오류가 발생해도 나머지는 정상작동한다.
        // RequestBody는 HttpMessageConverter단계에서 JSON데이터를 객체로 변경 못하면 예외가 발생하여 즉시 중지된다.

        log.info("API 컨트롤러 호출");

        if(bindingResult.hasErrors()){
            log.info("검증 오류 발생 = {}",bindingResult);
            return bindingResult.getAllErrors();
        }

        return form;
    }
}
