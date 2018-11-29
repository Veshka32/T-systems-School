package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import services.interfaces.HotTariffServiceI;

@RestController
public class HotTariffController {

    @Autowired
    HotTariffServiceI hotTariffService;

    @GetMapping(value = "/hotTariffs")
    public String sendHot() {
        hotTariffService.pushHots();
        return "";
    }
}
