package controllers;

import model.dto.MyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

@Controller
public class MainPageController {
    @Autowired
    TariffServiceI tariffServiceI;

    @Autowired
    OptionServiceI optionServiceI;

    @RequestMapping("/")
    public String root(Model model) {
        model.addAttribute("tariffs", tariffServiceI.getAll());
        model.addAttribute("options", optionServiceI.getAll());
        model.addAttribute("user", new MyUserDTO());
        return "/index";
    }
}
