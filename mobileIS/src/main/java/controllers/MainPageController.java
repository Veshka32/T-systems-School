package controllers;

import model.dto.AccountDTO;
import model.stateful.CartInterface;
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


    @Autowired
    CartInterface cartInterface;

    @RequestMapping({"/", "/index"})
    public String root(Model model) {
        model.addAttribute("tariffs", tariffServiceI.getAll());
        model.addAttribute("options", optionServiceI.getAll());
        model.addAttribute("user", new AccountDTO());
        model.addAttribute("cart", cartInterface);
        return "index";
    }
}
