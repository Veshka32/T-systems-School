package controllers;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import services.ServiceException;
import services.TariffServiceI;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TariffController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String CREATE = "management/tariff/create-tariff";
    private static final String EDIT = "management/tariff/edit-tariff";
    private static final String REDIRECT_EDIT = "redirect:/management/editTariff";

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/tariffs")
    public String showAll() {
        return "management/tariff/tariff-management";
    }

    @GetMapping("/management/showTariff")
    public String show(@RequestParam("id") int id, Model model) {
        Tariff tariff = tariffService.getFull(id);
        showTariff(model, tariff);
        return "management/tariff/save-result";
    }

    @GetMapping("/management/createTariff")
    public String createShow(Model model) {
        buildModelForCreate(model, new TariffDTO());
        return CREATE;
    }

    @PostMapping("/management/createTariff")
    public String create(@ModelAttribute("tariff") @Valid TariffDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            buildModelForCreate(model, dto);
            return CREATE;
        }
        try {
            tariffService.create(dto);
        } catch (ServiceException e) {
            buildModelForCreate(model, dto);
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return CREATE;
        }
        showTariff(model,tariffService.getFull(dto.getId()));
        return "management/tariff/save-result";
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,
                             @RequestParam(value = "update", required = false) Boolean updated,
                             @RequestParam(value = "error", required = false) String error,
                             Model model) {
        if (error != null) model.addAttribute("message", error);
        else if (updated != null) model.addAttribute("message", "updated");
        Tariff tariff = buildModel(id, model);
        model.addAttribute("editedTariff", tariff);
        return EDIT;
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute("editedTariff") @Valid Tariff editedTariff, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            buildModel(editedTariff.getId(), model);
            return EDIT;
        }
        try {
            tariffService.update(editedTariff);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, editedTariff.getId());
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/tariff/deleteOption")
    public String deleteOption(@RequestParam("id") int id, @RequestParam("option_id") int optionId, RedirectAttributes attr) {
        try {
            tariffService.deleteOption(id, optionId);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/tariff/addOption")
    public String addOption(@RequestParam("id") int id, @RequestParam("option_name") String optionName, RedirectAttributes attr) {
        try {
            tariffService.addOption(id, optionName);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, id);
        return REDIRECT_EDIT;
    }

    @GetMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id, RedirectAttributes attr) {
        try {
            tariffService.delete(id);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        return "redirect:/management/tariffs";
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs() {
        return tariffService.getAll();
    }

    private void setAttributesForUpdate(RedirectAttributes attr, int id) {
        attr.addAttribute("update", true);
        attr.addAttribute("id", id);
    }

    private Tariff buildModel(int id, Model model) {
        TariffTransfer transfer = tariffService.getTransfer(id);
        model.addAttribute("currentOptions", transfer.getTariff().getOptions());
        model.addAttribute("newOptions", transfer.getAll());
        return transfer.getTariff();
    }

    private void buildModelForCreate(Model model, TariffDTO dto) {
        model.addAttribute("tariff", dto);
        model.addAttribute("all", optionService.getAllNames());
    }

    private void showTariff(Model model, Tariff tariff) {
        model.addAttribute("newTariff", tariff);
        model.addAttribute("options", tariff.getOptions().stream().map(TariffOption::getName).collect(Collectors.joining(", ")));
    }
}
