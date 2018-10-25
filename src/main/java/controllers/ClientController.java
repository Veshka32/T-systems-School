package controllers;

import entities.Client;
import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ClientService;
import services.ContractService;
import services.Phone;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ClientController {

    /**
     * TODO ClientValidator (initBinder)
     *
     */

    @Autowired
    ClientService clientService;

    @Autowired
    ContractService contractService;

    @RequestMapping("/management/clients")
    public String show(){return "management/client/client-management";
    }

    @PostMapping("/management/findClientByPhone")
    public String find(@Valid Phone phone, BindingResult result, RedirectAttributes attr){
        if (result.hasErrors())
            return "management/client/client-management";
        int clientId=contractService.findClientByPhone(phone.getPhoneNumber());
        attr.addAttribute("clientId",clientId);
        return "redirect:/management/editClient";
    }

    @PostMapping("/management/createClient")
    public String create(@Valid Client client, BindingResult result,Model model){
        if (result.hasErrors())
            return "/management/client/create-client";
        int id=clientService.create(client);
       model.addAttribute("editedClient",clientService.get(id));
        return "management/client/edit-client";
    }

    @GetMapping("/management/createClient")
    public String createShow(){
        return "management/client/create-client";
    }


    @GetMapping("/management/editClient")
    public String editClient(@RequestParam("clientId") int id, Model model){
        model.addAttribute("editedClient",clientService.get(id));
        model.addAttribute("contracts",contractService.getAllClientContracts(id));
        return "management/client/edit-client";
    }

    @ModelAttribute("client")
    public Client formBackingObject() {
        return new Client();
    }

    @ModelAttribute("clients")
    public List<Client> showAllClients() {
        return clientService.getAll();
    }

    @ModelAttribute("phone")
    public Phone getPhone() {
        return new Phone();
    }
}
