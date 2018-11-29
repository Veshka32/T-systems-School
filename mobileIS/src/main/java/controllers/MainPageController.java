package controllers;

import model.stateful.CartInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

@Controller
public class MainPageController {
    private static final int OPTION_ON_PAGE = 10;
    private static final int TARIFF_ON_PAGE = 3;

    @Autowired
    TariffServiceI tariffServiceI;

    @Autowired
    OptionServiceI optionServiceI;

    @Autowired
    CartInterface cartInterface;

    @RequestMapping({"/", "/index"})
    public String root(Model model) {
        model.addAttribute("tariffs", tariffServiceI.getLast(TARIFF_ON_PAGE));
        model.addAttribute("options", optionServiceI.getPaginateData(1, OPTION_ON_PAGE).getItems());
        model.addAttribute("cart", cartInterface);
        return "index";
    }
}
