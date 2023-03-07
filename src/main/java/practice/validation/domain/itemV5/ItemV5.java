package practice.validation.domain.itemV5;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import practice.validation.domain.item.SaveCheck;
import practice.validation.domain.item.UpdateCheck;

@Data
public class ItemV5 {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemV5() {
    }

    public ItemV5(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
