package controllers;

import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionException;
import services.OptionServiceI;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OptionController {
    private static final String EDIT="management/option/edit-option";
    private static final String CREATE="management/option/create-option";
    private static final String REDIRECT_EDIT="redirect:/management/editOption";

    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/options")
    public String showAll() {
        return "management/option/option-management";
    }

    @GetMapping("/management/showOption")
    public String show(@RequestParam("id") int id, Model model) {
        TariffOption tariffOption = optionService.getFull(id);
        model.addAttribute("newOption", tariffOption);
        model.addAttribute("badOptions", tariffOption.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        model.addAttribute("mandatoryOptions", tariffOption.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        return "management/option/save-result";
    }

    @GetMapping("/management/createOption")
    public String createShow() {
        return CREATE;
    }

    @PostMapping("/management/createOption")
    public String create(@ModelAttribute("option") @Valid TariffOption option, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("option", option);
            return CREATE;
        }
        try {
            optionService.create(option);
        } catch (OptionException e) {
            model.addAttribute("error", e.getMessage());
            return CREATE;
        }
        model.addAttribute("newOption", option);
        model.addAttribute("badOptions", option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        model.addAttribute("mandatoryOptions", option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        return "/management/option/save-result";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id, @RequestParam(value = "update", required = false) Boolean updated, Model model) {
        TariffOption option = optionService.getFull(id);
        if (updated != null) model.addAttribute("updated", "updated");
        model.addAttribute("editedOption", option);
        model.addAttribute("currentIncompatible", option.getIncompatibleOptions());
        model.addAttribute("currentMandatory", option.getMandatoryOptions());
        List<String> allNames = optionService.getAllNames();
        model.addAttribute("all", allNames);
        model.addAttribute("all", allNames);
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOption dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return EDIT;
        }
        try {
            optionService.update(dto);
        } catch (OptionException e) {
            model.addAttribute("error", e.getMessage());
            return EDIT;
        }
        setAttributesForUpdate(attr,dto.getId());

        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/deleteIncompatibleOption")
    public String deleteIncompatibleOption(@RequestParam("id") int id, @RequestParam("option_id") int optionId, RedirectAttributes attr) {
        optionService.removeIncompatibleOption(id, optionId);
        attr.addAttribute("id", id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/addIncompatibleOption")
    public String addIncompatibleOption(@RequestParam("id") int id, @RequestParam("option_name") String optionName, RedirectAttributes attr, Model model) {
        try {
            optionService.addIncompatibleOption(id, optionName);
        } catch (OptionException e) {
            model.addAttribute("error", e.getMessage());
            return EDIT;
        }
        setAttributesForUpdate(attr,id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/deleteMandatoryOption")
    public String deleteMandatoryOption(@RequestParam("id") int id, @RequestParam("option_id") int optionId, RedirectAttributes attr) {
        optionService.removeMandatoryOption(id, optionId);
        attr.addAttribute("id", id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/addIncompatibleOption")
    public String addMandatoryOption(@RequestParam("id") int id, @RequestParam("option_name") String optionName, RedirectAttributes attr, Model model) {
        try {
            optionService.addMandatoryOption(id, optionName);
        } catch (OptionException e) {
            model.addAttribute("error", e.getMessage());
            return EDIT;
        }
        setAttributesForUpdate(attr,id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id) {
        optionService.delete(id);
        return "redirect:/management/options";
    }

    @ModelAttribute("option")
    public TariffOption formBackingObject() {
        return new TariffOption();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions() {
        return optionService.getAll();
    }

    private void setAttributesForUpdate(RedirectAttributes attr,int id){
        attr.addAttribute("update", true);
        attr.addAttribute("id", id);
    }
}
