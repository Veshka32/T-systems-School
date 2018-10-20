package controllers;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.OptionServiceI;
import services.TariffServiceI;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TariffCabinet {

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @PostMapping("/createTariff")
    public String create(@Valid Tariff tariff, BindingResult result){
        if (result.hasErrors()) return "create-tariff";
        String name=tariff.getName();
        tariffService.create(name);
        return "redirect:/tariffs";
    }

    @GetMapping("/createTariff")
    public String createShow(){
        return "create-tariff";
    }

    @GetMapping("/editTariff")
    public String editTariff(@RequestParam("id") int id,Model model){
        model.addAttribute("editedTariff",tariffService.get(id));
        return "edit-tariff";
    }

    @PostMapping("/editTariff")
    public String updateTariff(@Valid Tariff tariff, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()) return "edit-tariff";
        tariffService.update(tariff);
        attributes.addAttribute("id",tariff.getId());
        return "redirect:/editTariff";
    }

    @GetMapping("/deleteOption")
    public String deleteOption(@RequestParam("id") int id,@RequestParam("option_id") int option_id,RedirectAttributes attr){
        tariffService.deleteOption(id,option_id);
        attr.addAttribute("id",id);
        return "redirect:/editTariff";
    }

    @GetMapping("/deleteIncompatibleOption")
    public String deleteIncompatibleOption(@RequestParam("id") int id,@RequestParam("option_id") int option_id,RedirectAttributes attr){
        tariffService.deleteIncompatibleOption(id,option_id);
        attr.addAttribute("id",id);
        return "redirect:/editTariff";
    }

    @RequestMapping("/tariffs")
    public String show(ModelMap model){
//        List<Tariff> all=tariffService.getAll();
//        model.addAttribute("allTariffs",all);
        return "tariff-management";
    }

    @ModelAttribute("tariff")
    public Tariff formBackingObject() {
        return new Tariff();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAvailableIceCreams(){
        return optionService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs(){
        return tariffService.getAll();
    }
}
