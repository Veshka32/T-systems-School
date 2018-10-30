package controllers;

import entities.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.implementations.MyUserService;
import services.ServiceException;


@Controller
public class SignUpController {

    @Autowired
    MyUserService userService;

    @GetMapping(value = "/register")
    public String showRegister(Model model) {
       model.addAttribute("user",new MyUser());
       return "sign-up";
    }

    @PostMapping(value = "/register")
    public String addUser(@ModelAttribute("user") MyUser user, Model model, RedirectAttributes attr) {
        try{userService.create(user);}
        catch (ServiceException e){
            model.addAttribute("message",e.getMessage());
            return "sign-up";
        }
        attr.addAttribute("id",user.getContract().getId());
        return "redirect:/user/cabinet";
    }

}
