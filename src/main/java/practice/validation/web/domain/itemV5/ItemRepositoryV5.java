package practice.validation.web.domain.itemV5;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryV5 {

    private static final Map<Long, ItemV5> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public ItemV5 save(ItemV5 item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public ItemV5 findById(Long id) {
        return store.get(id);
    }

    public List<ItemV5> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, ItemV5 updateParam) {
        ItemV5 findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
