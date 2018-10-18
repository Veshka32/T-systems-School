package controllers;

import entities.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.TariffServiceI;

import java.util.List;

@Controller
public class CreateTariff {

    @Autowired
    TariffServiceI tariffService;

    @PostMapping("/createTariff")
    public String create(@RequestParam String name, Model model){
        tariffService.create(name);
        List<Tariff> all=tariffService.getAll();
        model.addAttribute("allTariffs",all);
        return "tariff-management";
    }

    @GetMapping("/createTariff")
    public String showAll(ModelMap model){
    List<Tariff> all=tariffService.getAll();
    model.addAttribute("allTariffs",all);
    System.out.println(model.get("allTariffs"));
        return "tariff-management";
    }
}
