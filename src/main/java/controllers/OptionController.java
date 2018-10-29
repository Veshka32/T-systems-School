package controllers;

import entities.TariffOption;
import entities.dto.TariffOptionDTO;
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
    private static final String MODEL_MESSAGE="message";

    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/options")
    public String showAll() {
        return "management/option/option-management";
    }

    @GetMapping("/management/showOption")
    public String show(@RequestParam("id") int id, Model model) {
        TariffOption option = optionService.getFull(id);
        showOption(model, option);
        return "management/option/save-result";
    }

    @GetMapping("/management/createOption")
    public String createShow(Model model) {
        buildModelForCreate(model, new TariffOptionDTO());
        return CREATE;
    }

    @PostMapping("/management/createOption")
    public String create(@ModelAttribute("option") @Valid TariffOptionDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            buildModelForCreate(model,dto);
            return CREATE;
        }
        try {
            optionService.create(dto);
        } catch (ServiceException e) {
            buildModelForCreate(model,dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return CREATE;
        }
        showOption(model,optionService.getFull(dto.getId()));
        return "/management/option/save-result";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        TariffOptionTransfer transfer = optionService.getTransferForEdit(id);
        buildModelForUpdate(model,transfer.getDTO());
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            buildModelForUpdate(model,dto);
            return EDIT;
        }
        try {
            optionService.update(dto);
        } catch (ServiceException e) {
            buildModelForUpdate(model,dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
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

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions() {
        return optionService.getAll();
    }

    private void showOption(Model model, TariffOption option) {
        model.addAttribute("newOption", option);
        model.addAttribute("badOptions", option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.joining(", ")));
        model.addAttribute("mandatoryOptions", option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.joining(", ")));
    }

    private void buildModelForCreate(Model model,TariffOptionDTO dto){
        model.addAttribute("option", dto);
        model.addAttribute("all",optionService.getAllNames());
    }

    private void buildModelForUpdate(Model model,TariffOptionDTO dto){
        model.addAttribute("editedOption",dto);
        model.addAttribute("all",optionService.getAllNames());
    }
}
