package controllers;

import entities.TariffOption;
import entities.TariffOptionTransfer;
import entities.dto.TariffOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.OptionServiceI;

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
        TariffOptionDTO dto = optionService.getFull(id);
        model.addAttribute("newOption", dto);
        model.addAttribute("badOptions", dto.getIncompatible().stream().collect(Collectors.joining(", ")));
        model.addAttribute("mandatoryOptions", dto.getMandatory().stream().collect(Collectors.joining(", ")));
        return "management/option/show-option";
    }

    @GetMapping("/management/createOption")
    public String createShow(Model model) {
        buildModelForCreate(model, new TariffOptionDTO());
        return CREATE;
    }

    @PostMapping("/management/createOption")
    public String create(@ModelAttribute("option") @Valid TariffOptionDTO dto, BindingResult result, Model model,RedirectAttributes attr) {
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
        attr.addAttribute("id",dto.getId());
        return "redirect:/management/showOption";
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        TariffOptionTransfer transfer = optionService.getTransferForEdit(id);
        String name=transfer.getDTO().getName();
        buildModelForUpdate(model,transfer.getDTO(),name);
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String updateOption(@ModelAttribute("editedOption") @Valid TariffOptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            buildModelForUpdate(model,dto,dto.getName());
            return EDIT;
        }
        try {
            optionService.update(dto);
        } catch (ServiceException e) {
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            buildModelForUpdate(model,dto,dto.getName());

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

    private void buildModelForCreate(Model model,TariffOptionDTO dto){
        model.addAttribute("option", dto);
        model.addAttribute("all",optionService.getAllNames());
    }

    private void buildModelForUpdate(Model model,TariffOptionDTO dto,String name){
        model.addAttribute("editedOption",dto);
        List<String> all=optionService.getAllNames();
        /**
         * TODO optimise removing
         */
        all.remove(name);
        model.addAttribute("all",all);
    }
}
