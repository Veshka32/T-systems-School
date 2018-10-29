package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.ClientService;

@Controller
public class UserCabinet {
    @Autowired
    ClientService clientService;

    @RequestMapping("/user/cabinet")
    public String create(@RequestParam("id") int id, Model model){
        model.addAttribute("user",clientService.get(id));
        return "user/user-cabinet";
    }
}
