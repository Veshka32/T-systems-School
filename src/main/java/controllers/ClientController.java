package controllers;

import entities.Client;
import entities.dto.ClientDTO;
import entities.dto.Passport;
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
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE="message";
    private static final String EDIT = "management/client/edit-client";
    private static final String SAVE="management/client/save-result";
    private static final String CREATE="management/client/create-client";

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
        return SAVE;
    }

    @PostMapping("/management/findClientByPhone")
    public String find(@Valid Phone phone, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("phone",phone);
            return "management/client/client-management";
        }

        Client client=contractService.findClientByPhone(Long.parseLong(phone.getPhoneNumber()));
        if (client==null){
            model.addAttribute(MODEL_MESSAGE,"phone number doesn't exist");
            return "management/client/client-management";
        }
        model.addAttribute("client",client);
        model.addAttribute("clientContracts",contractService.getAllClientContracts(client.getId()));
        return SAVE;
    }

    @PostMapping("/management/findClientByPassport")
    public String find(@Valid Passport passport, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("passport",passport);
            return "management/client/client-management";
        }

        Client client=clientService.findByPassport(passport.getPassport());
        if (client==null){
            model.addAttribute(MODEL_MESSAGE,"passport id doesn't exist");
            return "management/client/client-management";
        }
        model.addAttribute("client",client);
        model.addAttribute("clientContracts",contractService.getAllClientContracts(client.getId()));
        return SAVE;
    }

    @GetMapping("/management/createClient")
    public String createShow(Model model){
        model.addAttribute("client",new ClientDTO());
        return CREATE;
    }

    @PostMapping("/management/createClient")
    public String create(@Valid ClientDTO dto, BindingResult result,Model model){
        if (result.hasErrors()){
            model.addAttribute("client",dto);
            return CREATE;
        }
        try {
            clientService.create(dto);
        } catch (ServiceException e){
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            model.addAttribute("client",dto);
            return CREATE;
        }
       model.addAttribute("client",dto);
        return SAVE;
    }

    @GetMapping("/management/editClient")
    public String editClient(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model){
        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        model.addAttribute("editedClient",clientService.getDTO(id));
        return EDIT;
    }

    @PostMapping("/management/editClient")
    public String updateClient(@ModelAttribute("editedClient") @Valid ClientDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute("editedClient",dto);
            return EDIT;
        }
        try {
            clientService.update(dto);
        } catch (ServiceException e) {
            model.addAttribute("editedClient",dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return EDIT;
        }
        attr.addAttribute("id",dto.getId());
        return "redirect:/management/showClient";
    }

    @GetMapping("/management/deleteClient")
    public String deleteClient(@RequestParam("id") int id) {
        clientService.delete(id);
        return "redirect:/management/clients";
    }

    @ModelAttribute("allClients")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @ModelAttribute("phone")
    public Phone getPhone() {
        return new Phone();
    }

    @ModelAttribute("passport")
    public Passport passport() {
        return new Passport();
    }
}
