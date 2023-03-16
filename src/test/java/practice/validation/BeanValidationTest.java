package practice.validation;

import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import practice.web.domain.item.Item;

import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation(){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        Validator validator=factory.getValidator();
        // 검증 기능 (스프링과 통합시 직접 이 코드를 작성하지는 않는다.)

        Item item=new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        // Item을 검증한 결과를 받아온다.

        for(ConstraintViolation<Item> violation : violations){
            System.out.println("violation = "+violation);
            System.out.println("violation = "+violation.getMessage());
        }
        // 오류 발생 객체, 필드, 메시지 정보 등을 알 수 있다.
    }
}
