package controllers;

import model.dto.TariffDTO;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TariffController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE="message";
    private static final String CREATE = "management/tariff/create-tariff";
    private static final String EDIT = "management/tariff/edit-tariff";
    private static final String REDIRECT_EDIT = "redirect:/management/editTariff";
    private static final String TARIFF = "tariff";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all tariffs

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/tariffs")
    public String showAll(@RequestParam(value = "page", required = false) Integer page, Model model) {
        PaginateHelper<Tariff> helper = tariffService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
        return "management/tariff/tariff-management";
    }

    @GetMapping("/management/showTariff")
    public String show(@RequestParam("id") int id, Model model) {
        TariffDTO dto = tariffService.getDto(id);
        model.addAttribute(TARIFF, dto);
        model.addAttribute("options", dto.getOptions().stream().collect(Collectors.joining(", ")));
        return "management/tariff/show-tariff";
    }

    @GetMapping("/management/createTariff")
    public String createShow(Model model) {
        buildModel(model, new TariffDTO());
        return CREATE;
    }

    @PostMapping("/management/createTariff")
    public String create(@ModelAttribute(TARIFF) @Valid TariffDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            buildModel(model, dto);
            return CREATE;
        }
        try {
            attr.addAttribute("id", tariffService.create(dto));
            return "redirect:/management/showTariff";

        } catch (ServiceException e) {
            buildModel(model, dto);
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            return CREATE;
        }
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {
        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        buildModel(model, tariffService.getDto(id));
        return EDIT;
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute(TARIFF) @Valid TariffDTO dto, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            buildModel(model, dto);
            return EDIT;
        }
        try {
            tariffService.update(dto);
            attr.addAttribute("id", dto.getId());
            return "redirect:/management/showTariff";
        } catch (ServiceException e) {
            buildModel(model, dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return EDIT;
        }
    }

    @PostMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id, RedirectAttributes attr) {
        try {
            tariffService.delete(id);
            return "redirect:/management/tariffs";
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
            attr.addAttribute("id",id);
            return REDIRECT_EDIT;
        }
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs() {
        return tariffService.getAll();
    }

    private void buildModel(Model model, TariffDTO dto) {
        model.addAttribute(TARIFF, dto);
        model.addAttribute("all", optionService.getAllNames());
    }
}
