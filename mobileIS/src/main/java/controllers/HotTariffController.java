package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import services.interfaces.JmsSenderI;

@Controller
public class HotTariffController {

    @Autowired
    JmsSenderI jmsSender;

    @GetMapping(value = "/hotTariffs")
    public String sendHot() {
        jmsSender.sendData();
        return "blank";
    }
}
