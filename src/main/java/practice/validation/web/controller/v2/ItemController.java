package practice.validation.web.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.validation.domain.item.Item;
import practice.validation.domain.item.ItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/view/v2/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "view/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "view/v2/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {

        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item","price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000,1000000},null));
        }
        if(item.getQuantity()==null||item.getPrice()<=0||item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item","quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999},null));
        }
        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000,resultPrice}, null));
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v2/addForm";
        }
        // BindingResult는 검증 오류를 저장하고, 컨트롤러를 호출한다. errors 인터페이스를 상속받는 인터페이스로, @ModelAttribute 뒤에 와야 한다.
        // properties에 설정해둔 메시지 소스를 오류 메시지로 사용 가능하다. 여러개를 넣을 수 있기에 배열의 형태를 띈다.
        // 필드에 오류가 있으면 FieldError 객체를 bindingResult에 담는다.
        //      FieldError(@ModelAttribute객체, 필드명, 오류 기본 메시지)
        //      혹은 FieldError(@ModelAttribute객체, 필드명, 입력된 값, 바인딩/검증 실패 여부, 메시지 코드, 메시지 인자, 오류 기본 메시지)
        // 특정 필드를 넘어서는 오류는 ObjectError 객체를 bindingResult에 담는다.
        //      ObjectError(ModelAttribute객체, 오류 기본 메시지)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/view/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/view/v2/items/{itemId}";
    }

}

