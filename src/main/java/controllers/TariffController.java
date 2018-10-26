package controllers;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import services.TariffService;
import validators.TariffValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TariffController {

    @Autowired
    TariffService tariffService;
    @Autowired
    OptionServiceI optionService;

    @Autowired
    private TariffValidator tariffValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(tariffValidator);
    }

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
        tariffService.create(tariff);
        model.addAttribute("newTariff",tariff);
        model.addAttribute("tariffOptions",tariff.getOptions().stream().map(TariffOption::getName).collect(Collectors.joining(","));
        return "management/tariff/save-result";
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,@RequestParam(value = "updated",required = false) Boolean updated, Model model){
        Tariff tariff=tariffService.get(id);
        List<TariffOption> tariffOptions=tariffService.getTariffOptions(id);
        List<TariffOption> availableOptions=tariffService.getAvailableOptions(id);
        if (updated!=null) model.addAttribute("updated","updated");
        model.addAttribute("currentOption",tariffOptions);
        model.addAttribute("newOptions",availableOptions);
        return "management/tariff/edit-tariff";
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute("editedTariff") @Valid Tariff editedTariff, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()){
            return "management/tariff/edit-tariff";
        }
        tariffService.update(editedTariff);
        return "management/tariff/edit-tariff";
    }

    @GetMapping("/management/tariff/deleteOption")
    public String deleteOption(@RequestParam("id") int id,@RequestParam("option_id") int optionId,RedirectAttributes attr){
        tariffService.deleteOption(id,optionId);
        attr.addAttribute("id",id);
        attr.addAttribute("updated",true);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/tariff/addOption")
    public String addOption(@RequestParam("id") int id,@RequestParam("option_id") int optionId,RedirectAttributes attr){
        tariffService.addOption(id,optionId);
        attr.addAttribute("id",id);
        attr.addAttribute("updated",true);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id,RedirectAttributes attr){
        tariffService.delete(id);
        return    "redirect:/management/tariffs";
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
}
