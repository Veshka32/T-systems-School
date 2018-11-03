package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

@Controller
public class LoginController {
    @Autowired
    TariffServiceI tariffServiceI;

    @Autowired
    OptionServiceI optionServiceI;

    @RequestMapping("/")
    public String root(Model model) {
        model.addAttribute("tariffs", tariffServiceI.getAll());
        model.addAttribute("options", optionServiceI.getAll());
        return "/index";
    }

    @GetMapping("/homePage")
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/login")
    public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
                                  @RequestParam(value = "logout",	required = false) String logout) {

    ModelAndView model=new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid Credentials provided.");
        }

        if (logout != null) {
            model.addObject("message", "Logged out successfully.");
        }
       return model;
    }


}
