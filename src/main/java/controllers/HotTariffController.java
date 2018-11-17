package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller(value = "/hotTariffs")
public class HotTariffController {
    @Autowired
    JmsSender jmsSender;

    @GetMapping
    public String sendHot() {
        jmsSender.sendData();

        return "blank";
    }
}
