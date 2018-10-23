package controllers;

import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import validators.OptionValidator;
import javax.validation.Valid;
import java.util.List;

@Controller
public class OptionController {

    @Autowired
    OptionServiceI optionService;

    /**
     * TODO
     */
    @Autowired
    private OptionValidator optionValidator;

    /**
     * TODO
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(optionValidator);
    }


    @PostMapping("/management/createOption")
    public String create(@Valid TariffOption option, BindingResult result){
        if (result.hasErrors())
            return "management/option/create-option";
        optionService.create(option);
        return "redirect:/management/options";
    }

    @GetMapping("/management/createOption")
    public String createShow(){
        return "management/option/create-option";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,Model model){
        model.addAttribute("editedOption",optionService.get(id));
        return "management/option/edit-option";
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOption editedOption, BindingResult result){
        if (result.hasErrors()){
            return "management/option/edit-option";
        }
        optionService.update(editedOption);
        return "management/option/edit-option";
    }

    @GetMapping("/management/deleteIncompatibleOption")
    public String deleteIncompatibleOption(@RequestParam("id") int id,@RequestParam("option_id") int optionId,RedirectAttributes attr){
        optionService.removeIncompatibleOption(id,optionId);
        attr.addAttribute("id",id);
        return "redirect:/management/editOption";
    }

    @GetMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id){
        optionService.delete(id);
        return    "redirect:/management/options";
    }

    @RequestMapping("/management/options")
    public String show(){
        return "management/option/option-management";
    }

    @ModelAttribute("option")
    public TariffOption formBackingObject() {
        return new TariffOption();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions(){
        return optionService.getAll();
    }
}
