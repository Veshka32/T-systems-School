package controllers;

import entities.Client;
import entities.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ClientService;
import services.ContractService;
import entities.dto.Phone;
import services.ServiceException;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    ContractService contractService;

    @RequestMapping("/management/clients")
    public String show(){return "management/client/client-management";
    }

    @GetMapping("/management/showClient")
    public String show(@RequestParam("id") int id, Model model) {
        Client client = clientService.get(id);
        model.addAttribute("client",client);
        model.addAttribute("clientContracts",contractService.getAllClientContracts(id));
        return "management/client/save-result";
    }

    @PostMapping("/management/findClientByPhone")
    public String find(@Valid Phone phone, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("phone",phone);
            return "management/client/client-management";
        }

        Client client=null;
        try{
            client=contractService.findClientByPhone(Long.parseLong(phone.getPhoneNumber()));
        } catch (NoResultException e){
            model.addAttribute("message","phone number doesn't exist");
            return "management/client/client-management";
        }
        model.addAttribute("client",client);
        model.addAttribute("clientContracts",contractService.getAllClientContracts(client.getId()));
        return "management/client/save-result";
    }

    @GetMapping("/management/createClient")
    public String createShow(Model model){
        model.addAttribute("client",new ClientDTO());
        return "management/client/create-client";
    }

    @PostMapping("/management/createClient")
    public String create(@Valid ClientDTO dto, BindingResult result,Model model){
        if (result.hasErrors()){
            model.addAttribute("client",dto);
            return "/management/client/create-client";
        }
        try {
            clientService.create(dto);
        } catch (ServiceException e){
            model.addAttribute("message",e.getMessage());
            model.addAttribute("client",dto);
            return "/management/client/create-client";
        }
       model.addAttribute("client",dto);
        return "management/client/save-result";
    }

    @GetMapping("/management/editClient")
    public String editClient(@RequestParam("id") int id, Model model){
        model.addAttribute("editedClient",clientService.get(id));
        model.addAttribute("contracts",contractService.getAllClientContracts(id));
        return "management/client/edit-client";
    }

    @ModelAttribute("allClients")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @ModelAttribute("client")
    public Client formBackingObject() {
        return new Client();
    }

    @ModelAttribute("phone")
    public Phone getPhone() {
        return new Phone();
    }
}
