package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerCabinet {

    @RequestMapping("/manager")
    public String create(){
        return "manager-cabinet";
    }

    @RequestMapping("/")
    public String root(){
        return "index";
    }
}
