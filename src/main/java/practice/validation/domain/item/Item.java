package practice.validation.domain.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
// 스프링부트가 자동으로 Bean Validator를 인식하고 통합시킨다.
// @Validated 어노테이션이 들어간 메서드를 자동으로 글로벌 Validator로 등록시킨다.
// 단, 개발자가 Validator를 직접 등록하면 Bean Validator는 글로벌 Validator로 등록되지 않는다.
// 바인딩에 실패한 경우 그 상황에 맞는 FieldError를 추가한다.

@Data
public class Item {

    private Long id;

    @NotBlank(message = "공백 오류 메시지 (임의)")
    private String itemName;

    @NotNull
    @Range(min=1000,max=1000000)
    private Integer price;

    @NotNull
    @Range(max=9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
