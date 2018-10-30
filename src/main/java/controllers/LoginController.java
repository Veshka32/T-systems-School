package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String root(){
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
