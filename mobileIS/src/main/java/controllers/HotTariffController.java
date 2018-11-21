package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import services.implementations.JmsSender;

@Controller
public class HotTariffController {

    @Autowired
    JmsSender jmsSender;

    @GetMapping(value = "/hotTariffs")
    public String sendHot() {
        jmsSender.sendData();
        return "blank";
    }
}
