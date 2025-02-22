package practice.web.controller.v3;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import practice.web.domain.item.Item;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return Item.class.isAssignableFrom(clazz);
    }
    // 검증 지원 여부 확인

    @Override
    public void validate(Object target, Errors errors){
        Item item=(Item) target;

        if(!StringUtils.hasText(item.getItemName())){
            errors.rejectValue("itemName","required");
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            errors.rejectValue("price","range", new Object[]{1000,1000000},null);
        }
        if(item.getQuantity()==null||item.getPrice()<=0||item.getQuantity()>=9999){
            errors.rejectValue("quantity","max", new Object[]{9999},null);
        }
        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                errors.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }
    }
    // 오류 처리
}
