package practice.validation.web.controller.v3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.validation.domain.item.Item;
import practice.validation.domain.item.ItemRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/view/v3/items")
@RequiredArgsConstructor
public class ItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "view/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "view/v3/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v3/addForm";
        }
        // 숫자 대신 문자가 들어왔을 경우, 자료형의 불일치로 오류페이지가 먼저 나오게 된다.
        // 오류 정보를 가진 채로 입력 화면을 다시 불러와 개발자 선에서 오류처리를 할 수 있게 한다.

        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.rejectValue("itemName","required");
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000},null);
        }
        if(item.getQuantity()==null||item.getPrice()<=0||item.getQuantity()>=9999){
            bindingResult.rejectValue("quantity","max", new Object[]{9999},null);
        }
        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v3/addForm";
        }
        // 기존 addError와 FieldError의 장황한 요소를 보완한 rejectValue를 통해 오류를 전달할 수 있다.
        //      rejectValue(오류 필드명, 오류 코드, 오류 메시지 내 치환값, 기본 메시지)
        // 오류 코드가 많이 축약되었다. 다만 메시지 내용이 구체적이지 않다.
        // 더 세밀한 메시지 코드를 더 높은 우선순위로 사용하는 방식이 선호된다.
        // if문 없이 ValidationUtils를 통해서 간결한 오류 메시지 처리도 가능하다.

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/view/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/view/v3/items/{itemId}";
    }

}

