package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerCabinet {
    @RequestMapping("/management/cabinet")
    public String cabinet() {
        return "management/manager-cabinet";
    }

    @RequestMapping("/management")
    public String cabinet1() {
        return "redirect:/management/cabinet";
    }
}
