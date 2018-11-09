package controllers;

import entities.dto.MyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.MyUserServiceI;


@Controller
public class SignUpController {

    @Autowired
    MyUserServiceI userService;

    @GetMapping(value = "/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new MyUserDTO());
       return "sign-up";
    }

    @PostMapping(value = "/register")
    public String addUser(@ModelAttribute("user") MyUserDTO dto, Model model, RedirectAttributes attr) {
        try {
            attr.addAttribute("id", userService.create(dto));
            return "redirect:/user/cabinet";
        }
        catch (ServiceException e){
            model.addAttribute("message",e.getMessage());
            return "sign-up";
        }
    }

}
