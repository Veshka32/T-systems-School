package controllers;

import model.dto.TariffDTO;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.interfaces.HotTariffServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class TariffController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE = "message";
    private static final String CREATE = "management/tariff/create-tariff";
    private static final String REDIRECT_EDIT = "redirect:/management/editTariff";
    private static final String TARIFF = "tariff";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all tariffs

    private static final Logger logger = Logger.getLogger(ClientController.class);

    @Autowired
    TariffServiceI tariffService;

    @Autowired
    OptionServiceI optionService;

    @Autowired
    HotTariffServiceI hotTariffService;

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
        model.addAttribute(TARIFF, tariffService.getDto(id));
        return "management/tariff/show-tariff";
    }

    @GetMapping("/management/createTariff")
    public String create(Model model) {
        buildModel(model, new TariffDTO());
        return CREATE;
    }

    @PostMapping("/management/createTariff")
    public String create(@ModelAttribute(TARIFF) @Valid TariffDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            buildModel(model, dto);
            return CREATE;
        }

        Optional<String> error = tariffService.create(dto);
        if (error.isPresent()) {
            buildModel(model, dto);
            model.addAttribute(MODEL_MESSAGE, error.get());
            return CREATE;
        }

        attr.addAttribute("id", dto.getId());
        hotTariffService.pushHots();
            return "redirect:/management/showTariff";

    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {
        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        buildModel(model, tariffService.getDto(id));
        return CREATE;
    }

    @PostMapping("/management/editTariff")
    public String editTariff(@ModelAttribute(TARIFF) @Valid TariffDTO dto, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            buildModel(model, dto);
            return CREATE;
        }
        Optional<String> error = tariffService.update(dto);
        if (error.isPresent()) {
            buildModel(model, dto);
            model.addAttribute(MODEL_MESSAGE, error.get());
            return CREATE;
        }
            tariffService.update(dto);
            attr.addAttribute("id", dto.getId());
        hotTariffService.pushIfHots(dto.getId());
            return "redirect:/management/showTariff";

    }

    @PostMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<String> error = tariffService.delete(id);
        if (error.isPresent()) {
            attr.addAttribute(ERROR_ATTRIBUTE, error.get());
            attr.addAttribute("id", id);
            return REDIRECT_EDIT;
        }
        hotTariffService.pushIfHots(id);
            return "redirect:/management/tariffs";

    }

    private void buildModel(Model model, TariffDTO dto) {
        model.addAttribute(TARIFF, dto);
        model.addAttribute("all", optionService.getAllNamesWithIds());
    }

    @ExceptionHandler({NullPointerException.class})
    public String catchWrongId(HttpServletRequest request, Exception ex) {
        logger.warn("Request: " + request.getRequestURL(), ex);
        return "redirect:/management/tariffs";
    }
}
