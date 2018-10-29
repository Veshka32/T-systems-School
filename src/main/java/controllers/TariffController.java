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
    private static final String MODEL_MESSAGE="message";
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
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            return CREATE;
        }
        showTariff(model,tariffService.getFull(dto.getId()));
        return "management/tariff/save-result";
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {
        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        TariffTransfer transfer = tariffService.getTransferForEdit(id);
        model.addAttribute("editedTariff",transfer.getDto());
        model.addAttribute("all", transfer.getAll());
        return EDIT;
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute("editedTariff") @Valid TariffDTO dto, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            buildModelForUpdate(model,dto);
            return EDIT;
        }
        try {
            tariffService.update(dto);
        } catch (ServiceException e) {
            buildModelForUpdate(model,dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return EDIT;
        }
        attr.addAttribute("id",dto.getId());
        return "redirect:/management/showTariff";
    }


    @GetMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id, RedirectAttributes attr) {
        try {
            tariffService.delete(id);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
            attr.addAttribute("id",id);
            return REDIRECT_EDIT;
        }
        return "redirect:/management/tariffs";
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs() {
        return tariffService.getAll();
    }

    private void buildModelForCreate(Model model, TariffDTO dto) {
        model.addAttribute("tariff", dto);
        model.addAttribute("all", optionService.getAllNames());
    }

    private void buildModelForUpdate(Model model,TariffDTO dto){
        model.addAttribute("editedTariff",dto);
        List<String> map = optionService.getAllNames(); //do not include archived optionsNames
        model.addAttribute("all",map);
    }

    private void showTariff(Model model, Tariff tariff) {
        model.addAttribute("newTariff", tariff);
        model.addAttribute("options", tariff.getOptions().stream().map(TariffOption::getName).collect(Collectors.joining(", ")));
    }
}
