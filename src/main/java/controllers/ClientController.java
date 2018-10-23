package controllers;

import entities.Client;
import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ContractServiceI;
import services.OptionServiceI;
import services.TariffServiceI;
import validators.TariffValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ClientController {

    /**
     * TODO clientService, ClientValidator (initBinder)
     *
     */

    @RequestMapping("/management/clients")
    public String show(){
        return "management/client/client-management";
    }

    @PostMapping("/management/createClient")
    public String create(@Valid Client client, BindingResult result){
        if (result.hasErrors())
            return "/management/client/create-client";
        //contractService.create(client);
        return "redirect:/management/client/edit-client";
    }

    @GetMapping("/management/createClient")
    public String createShow(){
        return "management/client/create-client";
    }

    @ModelAttribute("client")
    public Client formBackingObject() {
        return new Client();
    }

}
