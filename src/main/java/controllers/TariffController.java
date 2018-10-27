package controllers;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import services.ServiceException;
import services.TariffServiceI;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TariffController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String EDIT = "management/tariff/edit-tariff";

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @RequestMapping("/management/tariffs")
    public String showAll(){
        return "management/tariff/tariff-management";
    }

    @GetMapping("/management/createTariff")
    public String createShow(){
        return "management/tariff/create-tariff";
    }

    @PostMapping("/management/createTariff")
    public String create(@ModelAttribute("tariff") @Valid Tariff tariff, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("tariff",tariff);
            return "management/tariff/create-tariff";}
        try{tariffService.create(tariff);}
        catch (ServiceException e){
            model.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
            return "/management/tariff/create-tariff";
        }
        model.addAttribute("newTariff",tariff);
        model.addAttribute("tariffOptions",tariff.getOptions().stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        return "management/tariff/save-result";
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,
                             @RequestParam(value = "update",required = false) Boolean updated,
                             @RequestParam(value = "error", required = false) String error,
                             Model model){
        if (error!=null) model.addAttribute("message",error);
        else if (updated!=null) model.addAttribute("message","updated");
        Tariff tariff=buildModel(id,model);
        model.addAttribute("editedTariff",tariff);
        return EDIT;
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute("editedTariff") @Valid Tariff editedTariff, BindingResult result, RedirectAttributes attr,Model model){
        if (result.hasErrors()){
            buildModel(editedTariff.getId(), model);
            return EDIT;
        }
        try {
            tariffService.update(editedTariff);
        } catch (ServiceException e) {
            attr.addAttribute(ERROR_ATTRIBUTE, e.getMessage());
        }
        setAttributesForUpdate(attr, editedTariff.getId());
        return EDIT;
    }

    @GetMapping("/management/tariff/deleteOption")
    public String deleteOption(@RequestParam("id") int id,@RequestParam("option_id") int optionId,RedirectAttributes attr){
        try {
            tariffService.deleteOption(id,optionId);
        } catch (ServiceException e){
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
        }
        setAttributesForUpdate(attr,id);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/tariff/addOption")
    public String addOption(@RequestParam("id") int id,@RequestParam("option_id") int optionId,RedirectAttributes attr){
        try {
            tariffService.addOption(id,optionId);
        } catch (ServiceException e){
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
        }
        setAttributesForUpdate(attr,id);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id,RedirectAttributes attr){
        try{tariffService.delete(id);}
        catch (ServiceException e){
            attr.addAttribute(ERROR_ATTRIBUTE,e.getMessage());
        }
        return "redirect:/management/tariffs";
    }

    @ModelAttribute("tariff")
    public Tariff formBackingObject() {
        return new Tariff();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions(){
        return optionService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs(){
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
}
