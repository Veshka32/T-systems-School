package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerCabinet {
    @RequestMapping("/management/cabinet")
    public String create(){
        return "management/manager-cabinet";
    }
}
