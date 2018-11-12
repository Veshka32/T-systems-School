package controllers;

import model.dto.MyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.UserServiceI;

import javax.validation.Valid;


@Controller
public class AuthController {
    private static final String SIGN_UP = "sign-up";

    @Autowired
    UserServiceI userService;

    @GetMapping(value = "/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new MyUserDTO());
        return SIGN_UP;
    }

    @PostMapping(value = "/register")
    public String addUser(@ModelAttribute("user") @Valid MyUserDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return SIGN_UP;
        }
        try {
            attr.addAttribute("id", userService.createClient(dto));
            model.addAttribute("message1", "Success! Now you can log in");
            return "redirect:/user/cabinet";
        }
        catch (ServiceException e){
            model.addAttribute("message1", e.getMessage());
            return SIGN_UP;
        }
    }

    @GetMapping(value = "/login")
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("message", "Invalid Credentials provided.");
        }

        if (logout != null) {
            model.addObject("message", "Logged out successfully.");
        }
        return model;
    }

}
