package practice.validation.web.controller.v4;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.validation.web.domain.item.Item;
import practice.validation.web.domain.item.ItemRepository;
import practice.validation.web.domain.item.SaveCheck;
import practice.validation.web.domain.item.UpdateCheck;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/view/v4/items")
@RequiredArgsConstructor
public class ItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "view/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "view/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        // @Validated가 아닌 @Valid도 가능하다.
        // 전자는 스프링 전용, 후자는 자바 표준 검증 어노테이션이다.
        // 다만 groups 기능을 쓰려면 @Validated를 사용해야 한다.

        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }
        // 기존 오브젝트 에러 검증 코드가 돌아왔다.
        // @ScriptAssert를 Item 클래스에 선언하여 처리할 수 있으나, java 15부터는 지원되지 않는다.

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v4/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/view/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v4/editForm";
        }
        // 입력 폼에서 했던 것처럼, 수정 폼에서도 검증 단계를 거치도록 한다.

        itemRepository.update(itemId, item);
        return "redirect:/view/v4/items/{itemId}";
    }

}

