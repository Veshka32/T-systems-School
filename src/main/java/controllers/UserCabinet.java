package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.ClientService;
import services.ContractService;

import java.security.Principal;

@Controller
public class UserCabinet {
    @Autowired
    ContractService contractService;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user){
        model.addAttribute("user",contractService.findClientByPhone(Long.parseLong(user.getName())));
        return "user/user-cabinet";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
