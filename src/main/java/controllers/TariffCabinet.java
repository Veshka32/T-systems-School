package controllers;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import services.OptionServiceI;
import services.TariffServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TariffCabinet {

    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;

    @PostMapping("/createTariff")
    public String create(Tariff tariff){
        String name=tariff.getName();
        tariffService.create(name);
        return "redirect:/createTariff";
    }

    @GetMapping("/createTariff")
    public String createShow(){
        return "create-tariff";
    }

    @ModelAttribute("tariff")
    public Tariff formBackingObject() {
        return new Tariff();
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAvailableIceCreams(){
        return optionService.getAll();
        //return optionService.getAll().stream().collect(Collectors.toMap(TariffOption::getName,c->c));
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
}
