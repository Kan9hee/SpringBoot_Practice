package practice.validation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import practice.validation.domain.item.Item;
import practice.validation.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/view/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "view/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "view/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model) {

        Map<String,String> errors=new HashMap<>();

        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName","상품명 오류 발생");
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            errors.put("price","가격 범위 오류 발생");
        }
        if(item.getQuantity()==null||item.getPrice()<=0||item.getQuantity()>=9999){
            errors.put("quantity","상품 수량 오류 발생");
        }
        if(item.getPrice()!=null&&item.getQuantity()!=null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice<10000){
                errors.put("globalError","상품 최소 총합 금액 오류 발생");
            }
        }
        if(!errors.isEmpty()){
            log.info("errors={}",errors);
            model.addAttribute("errors",errors);
            return "view/addForm";
        }
        //상품 등록 과정에서 오류 검증을 위한 로직.
        //검증 필드에 따른 오류를 errors에 담아 뷰 템플릿에 보낸다.
        //다만 이 로직은 타입 오류 처리를 할 수 없고, 고객이 입력한 값을 별도로 관리할 수 없다.

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/view/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "view/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/view/items/{itemId}";
    }

}

