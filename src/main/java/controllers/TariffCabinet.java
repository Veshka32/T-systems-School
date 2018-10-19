package controllers;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
        return "tariff-management"; //wrong
    }

    @GetMapping("/createTariff")
    public String createShow(){
        return "create-tariff";
    }

    @RequestMapping("/editTariff")
    public String editTariff(@RequestParam("id") int id,Model model){
        model.addAttribute("id",id);
        return "edit-tariff";
    }


    @RequestMapping("/tariffs")
    public String show(ModelMap model){
        List<Tariff> all=tariffService.getAll();
        model.addAttribute("allTariffs",all);
        return "tariff-management";
    }

    @RequestMapping("/newTariff")
    public String newTariff(){
        return "create-tariff";
    }

    @ModelAttribute("tariff")
    public Tariff formBackingObject() {
        return new Tariff();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAvailableIceCreams(){
        return optionService.getAll();
    }
}
