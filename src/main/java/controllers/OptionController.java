package controllers;

import model.dto.OptionDTO;
import model.entity.Option;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.exceptions.ServiceException;
import services.interfaces.OptionServiceI;

import javax.validation.Valid;

@Controller
public class OptionController {
    private static final String EDIT = "management/option/edit-option";
    private static final String CREATE = "management/option/create-option";
    private static final String REDIRECT_EDIT = "redirect:/management/editOption";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE = "message";
    private static final String OPTION = "option";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all options

    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/options")
    public String showAll(@RequestParam(value = "page", required = false) Integer page, Model model) {
        PaginateHelper<Option> helper = optionService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
        return "management/option/option-management";
    }

    @GetMapping("/management/showOption")
    public String show(@RequestParam("id") int id, Model model) {
        OptionDTO dto = optionService.getDto(id);
        model.addAttribute(OPTION, dto);
        return "management/option/show-option";
    }

    @GetMapping("/management/createOption")
    public String createShow(Model model) {
        model.addAttribute(OPTION, new OptionDTO());
        model.addAttribute("all", optionService.getAllNamesWithIds());
        return CREATE;
    }

    @PostMapping("/management/createOption")
    public String create(@ModelAttribute(OPTION) @Valid OptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute(OPTION, dto);
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return CREATE;
        }
        try {
            attr.addAttribute("id", optionService.create(dto));
            return "redirect:/management/showOption";
        } catch (ServiceException e) {
            model.addAttribute(OPTION, dto);
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return CREATE;
        }
    }

    @GetMapping("/management/editOption")
    public String editOption(@RequestParam("id") int id, @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error, Model model) {

        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        OptionDTO dto = optionService.getDto(id);
        model.addAttribute(OPTION, dto);
        model.addAttribute("all", optionService.getAllNamesWithIds());
        return EDIT;
    }

    @PostMapping("/management/editOption")
    public String editOption(@ModelAttribute(OPTION) @Valid OptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute(OPTION, dto);
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return EDIT;
        }
        try {
            optionService.update(dto);
            attr.addAttribute("id", dto.getId());
            return "redirect:/management/showOption";
        } catch (ServiceException e) {
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            model.addAttribute(OPTION, dto);
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return EDIT;
        }
    }

    @PostMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id, RedirectAttributes attr) {
        try {
            optionService.delete(id);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            attr.addAttribute("id", id);
            return REDIRECT_EDIT;
        }
        return "redirect:/management/options";
    }
}
