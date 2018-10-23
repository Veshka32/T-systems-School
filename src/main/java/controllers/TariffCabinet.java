package controllers;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import services.TariffServiceI;
import validators.TariffValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TariffCabinet {

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @Autowired
    private TariffValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }


    @PostMapping("/management/createTariff")
    public String create(@Valid Tariff tariff, BindingResult result){
        if (result.hasErrors())
            return "management/create-tariff";
        tariffService.create(tariff);
        return "redirect:/management/tariffs";
    }

    @GetMapping("/management/createTariff")
    public String createShow(){
        return "management/create-tariff";
    }

    @GetMapping("/management/editTariff")
    public String editTariff(@RequestParam("id") int id,Model model){
        model.addAttribute("editedTariff",tariffService.get(id));
        return "management/edit-tariff";
    }

    @PostMapping("/management/editTariff")
    public String updateTariff(@ModelAttribute("editedTariff") @Valid Tariff editedTariff, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()){
            return "management/edit-tariff";
        }
        tariffService.update(editedTariff);
        return "management/edit-tariff";
    }

    @GetMapping("/management/deleteOption")
    public String deleteOption(@RequestParam("id") int id,@RequestParam("option_id") int option_id,RedirectAttributes attr){
        tariffService.deleteOption(id,option_id);
        attr.addAttribute("id",id);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/deleteIncompatibleOption")
    public String deleteIncompatibleOption(@RequestParam("id") int id,@RequestParam("option_id") int option_id,RedirectAttributes attr){
        tariffService.deleteIncompatibleOption(id,option_id);
        attr.addAttribute("id",id);
        return "redirect:/management/editTariff";
    }

    @GetMapping("/management/deleteTariff")
    public String deleteTariff(@RequestParam("id") int id,RedirectAttributes attr){
        tariffService.delete(id);
        return    "redirect:/management/tariffs";
    }

    @RequestMapping("/management/tariffs")
    public String show(){
        return "management/tariff-management";
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
