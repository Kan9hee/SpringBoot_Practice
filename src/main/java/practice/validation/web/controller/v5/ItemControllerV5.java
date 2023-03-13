package practice.validation.web.controller.v5;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.validation.domain.itemV5.ItemV5;
import practice.validation.domain.itemV5.ItemRepositoryV5;
import practice.validation.web.controller.form.ItemSaveForm;
import practice.validation.web.controller.form.ItemUpdateForm;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemControllerV5 {

    private final ItemRepositoryV5 itemRepositoryV5;

    @GetMapping
    public String items(Model model) {
        List<ItemV5> items = itemRepositoryV5.findAll();
        model.addAttribute("items", items);
        return "view/v5/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        ItemV5 item = itemRepositoryV5.findById(itemId);
        model.addAttribute("item", item);
        return "view/v5/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new ItemV5());
        return "view/v5/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute에 별도로 item을 넣었는데, 현재 view페이지의 값은 item으로 설정되어 있다.
        // 그러나 현재 상태에서 전달하면 form이 전달되어 불일치가 발생한다.
        // 그래서 item으로 바꿔 보내는 것이다.

        if(form.getPrice()!=null&&form.getQuantity()!=null){
            int resultPrice = form.getPrice()*form.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v5/addForm";
        }

        ItemV5 itemV5=new ItemV5();
        itemV5.setItemName(form.getItemName());
        itemV5.setPrice(form.getPrice());
        itemV5.setQuantity(form.getQuantity());

        ItemV5 savedItem=itemRepositoryV5.save(itemV5);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        ItemV5 item = itemRepositoryV5.findById(itemId);
        model.addAttribute("item", item);
        return "view/v5/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        if(form.getPrice()!=null&&form.getQuantity()!=null){
            int resultPrice = form.getPrice()*form.getQuantity();
            if(resultPrice<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "view/v5/editForm";
        }

        ItemV5 itemParam=new ItemV5();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());

        itemRepositoryV5.update(itemId, itemParam);
        return "redirect:/items/{itemId}";
    }

}

