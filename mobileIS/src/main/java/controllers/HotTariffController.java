package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import services.interfaces.JmsSenderI;

@RestController
public class HotTariffController {

    @Autowired
    JmsSenderI jmsSender;

    @GetMapping(value = "/hotTariffs")
    public String sendHot() {
        jmsSender.sendData();
        return "";
    }
}
