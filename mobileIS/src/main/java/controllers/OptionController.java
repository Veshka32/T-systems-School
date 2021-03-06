package controllers;

import model.dto.OptionDTO;
import model.entity.Option;
import model.helpers.PaginateHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.interfaces.OptionServiceI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class OptionController {
    private static final String EDIT = "management/option/create-option";
    private static final String REDIRECT_EDIT = "redirect:/management/editOption";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE = "message";
    private static final String OPTION = "option";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all options

    private static final Logger logger = Logger.getLogger(OptionController.class);

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
        return EDIT;
    }

    @PostMapping("/management/createOption")
    public String create(@ModelAttribute(OPTION) @Valid OptionDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute(OPTION, dto);
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return EDIT;
        }

        Optional<String> error = optionService.create(dto);
        if (error.isPresent()) {
            model.addAttribute(OPTION, dto);
            model.addAttribute(MODEL_MESSAGE, error.get());
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return EDIT;
        }

            attr.addAttribute("id", dto.getId());
            return "redirect:/management/showOption";

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

        Optional<String> error = optionService.update(dto);
        if (error.isPresent()) {
            model.addAttribute(MODEL_MESSAGE, error.get());
            model.addAttribute(OPTION, dto);
            model.addAttribute("all", optionService.getAllNamesWithIds());
            return EDIT;
        }
            attr.addAttribute("id", dto.getId());
            return "redirect:/management/showOption";

    }

    @PostMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<String> error = optionService.delete(id);
        if (error.isPresent()) {
            attr.addAttribute(ERROR_ATTRIBUTE, error.get());
            attr.addAttribute("id", id);
            return REDIRECT_EDIT;
        }
            return "redirect:/management/options";
    }

    @ExceptionHandler({NullPointerException.class})
    public String catchWrongId(HttpServletRequest request, Exception ex) {
        logger.warn("Request: " + request.getRequestURL(), ex);
        return "redirect:/management/options";
    }
}
