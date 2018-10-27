package controllers;

import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.OptionServiceI;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OptionController {
    private static final String EDIT = "management/option/edit-option";
    private static final String CREATE = "management/option/create-option";
    private static final String REDIRECT_EDIT = "redirect:/management/editOption";
    private static final String ERROR_ATTRIBUTE = "error";

    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/options")
    public String showAll() {
        return "management/option/option-management";
    }

    @GetMapping("/management/showOption")
    public String show(@RequestParam("id") int id, Model model) {
        TariffOption option = optionService.getFull(id);
        showTariff(model, option);
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
        } catch (ServiceException e) {
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return CREATE;
        }
        showTariff(model,option);
        return "/management/option/save-result";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = "update", required = false) Boolean updated,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute("message", error);
        else if (updated != null) model.addAttribute("message", "updated");
        TariffOptionTransfer transfer = optionService.getTransfer(id);
        model.addAttribute("editedOption",transfer.getDTO());
        model.addAttribute("all", transfer.getAll());
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            TariffOptionTransfer transfer = optionService.getTransfer(dto.getId());
            model.addAttribute("editedOption",dto);
            model.addAttribute("all", transfer.getAll());
            return EDIT;
        }
        try {
            optionService.update(dto);
        } catch (ServiceException e) {
            TariffOptionTransfer transfer = optionService.getTransfer(dto.getId());
            model.addAttribute("editedOption",transfer.getDTO());
            model.addAttribute("all", transfer.getAll());
            model.addAttribute("message",e.getMessage());
            return EDIT;
        }
        attr.addAttribute("id",dto.getId());
        return "redirect:/management/showOption";
    }

    @GetMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id,RedirectAttributes attr) {
        try{optionService.delete(id);}
        catch (ServiceException e){
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
            attr.addAttribute("id",id);
            return REDIRECT_EDIT;
        }
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

    private void setAttributesForUpdate(RedirectAttributes attr, int id) {
        attr.addAttribute("update", true);
        attr.addAttribute("id", id);
    }

    private void showTariff(Model model, TariffOption option) {
        model.addAttribute("newOption", option);
        model.addAttribute("badOptions", option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        model.addAttribute("mandatoryOptions", option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
    }
}
