package controllers;

import entities.TariffOption;
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
            model.addAttribute("error", e.getMessage());
            return CREATE;
        }
        showTariff(model,option);
        return "/management/option/save-result";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = "update", required = false) Boolean updated,
                             @RequestParam(value = "errorMessage", required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute("message", error);
        else if (updated != null) model.addAttribute("message", "updated");
        TariffOption option = buildModel(id, model);
        model.addAttribute("editedOption", option);
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOption dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            buildModel(dto.getId(), model);
            return EDIT;
        }
        try {
            optionService.update(dto);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, dto.getId());
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
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/deleteMandatoryOption")
    public String deleteMandatoryOption(@RequestParam("id") int id, @RequestParam("option_id") int optionId, RedirectAttributes attr) {
        optionService.removeMandatoryOption(id, optionId);
        attr.addAttribute("id", id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/option/addMandatoryOption")
    public String addMandatoryOption(@RequestParam("id") int id, @RequestParam("option_name") String optionName, RedirectAttributes attr, Model model) {
        try {
            optionService.addMandatoryOption(id, optionName);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id,RedirectAttributes attr) {
        try{optionService.delete(id);}
        catch (ServiceException e){
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
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

    private TariffOption buildModel(int id, Model model) {
        TariffOptionTransfer transfer = optionService.getTransfer(id);
        model.addAttribute("currentIncompatible", transfer.getOption().getIncompatibleOptions());
        model.addAttribute("currentMandatory", transfer.getOption().getMandatoryOptions());
        model.addAttribute("all", transfer.getAll());
        return transfer.getOption();
    }

    private void showTariff(Model model, TariffOption option) {
        model.addAttribute("newOption", option);
        model.addAttribute("badOptions", option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        model.addAttribute("mandatoryOptions", option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
    }
}
