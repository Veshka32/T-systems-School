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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class OptionController {

    @Autowired
    OptionServiceI optionService;

    @Autowired
    private OptionValidator optionValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(optionValidator);
    }

    @RequestMapping("/management/options")
    public String show(){
        return "management/option/option-management";
    }

    @GetMapping("/management/createOption")
    public String createShow(){ return "management/option/create-option";
    }

    @PostMapping("/management/createOption")
    public String create(@Valid TariffOption option, BindingResult result, Model model){
        if (result.hasErrors())
            return "management/option/create-option";
        optionService.save(option);
        model.addAttribute("newOption",option);
        model.addAttribute("badOptions",option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        return "/management/option/save-result";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,Model model){
        TariffOption option=optionService.get(id);
        model.addAttribute("editedOption",option);
        model.addAttribute("addIncompatible",newIncompatible(option));
        model.addAttribute("currentIncompatible",option.getIncompatibleOptions());
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

    @GetMapping("/management/option/deleteIncompatibleOption")
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

    @ModelAttribute("option")
    public TariffOption formBackingObject() {
        return new TariffOption();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions(){
        return optionService.getAll();
    }

    private List<String> newIncompatible(TariffOption option){
        Set<TariffOption> incompatible=option.getIncompatibleOptions();
        return optionService.getAll().stream()
                .filter(o->!(incompatible.contains(o) || o.equals(option)))
                .map(TariffOption::getName)
                .collect(Collectors.toList());
    }
}
