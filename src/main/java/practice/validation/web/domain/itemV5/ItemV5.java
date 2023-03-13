package practice.validation.web.domain.itemV5;

import lombok.Data;

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
