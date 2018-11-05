package controllers;

import entities.Client;
import entities.dto.ClientDTO;
import entities.helpers.Passport;
import entities.helpers.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.ServiceException;
import services.interfaces.ClientServiceI;
import services.interfaces.ContractServiceI;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ClientController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE="message";
    private static final String EDIT = "management/client/edit-client";
    private static final String SAVE="management/client/show-client";
    private static final String CREATE="management/client/create-client";
    private static final String MANAGEMENT="management/client/client-management";
    private static final String CLIENT = "client";
    private static final String CONTRACTS = "clientContracts";

    @Autowired
    ClientServiceI clientService;

    @Autowired
    ContractServiceI contractService;

    @RequestMapping("/management/clients")
    public String show(){return MANAGEMENT;
    }

    @GetMapping("/management/showClient")
    public String show(@RequestParam("id") int id, Model model) {
        Client client = clientService.get(id);
        model.addAttribute(CLIENT, client);
        model.addAttribute(CONTRACTS, contractService.getAllClientContracts(id));
        return SAVE;
    }

    @PostMapping("/management/findClientByPhone")
    public String find(@Valid Phone phone, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("phone",phone);
            return MANAGEMENT;
        }

        Client client=contractService.findClientByPhone(Long.parseLong(phone.getPhoneNumber()));
        if (client==null){
            model.addAttribute(MODEL_MESSAGE,"phone number doesn't exist");
            return MANAGEMENT;
        }
        model.addAttribute(CLIENT, client);
        model.addAttribute(CONTRACTS, contractService.getAllClientContracts(client.getId()));
        return SAVE;
    }

    @PostMapping("/management/findClientByPassport")
    public String find(@Valid Passport passport, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("passport",passport);
            return MANAGEMENT;
        }

        Client client = clientService.findByPassport(passport.getPassportNumber());
        if (client==null){
            model.addAttribute("message1", "passportNumber id doesn't exist");
            return MANAGEMENT;
        }
        model.addAttribute(CLIENT, client);
        model.addAttribute(CONTRACTS, contractService.getAllClientContracts(client.getId()));
        return SAVE;
    }

    @GetMapping("/management/createClient")
    public String createShow(Model model){
        model.addAttribute(CLIENT, new ClientDTO());
        return CREATE;
    }

    @PostMapping("/management/createClient")
    public String create(@ModelAttribute(CLIENT) @Valid ClientDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }
        try {
            clientService.create(dto);
        } catch (ServiceException e){
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }
        model.addAttribute(CLIENT, dto);
        return SAVE;
    }

    @GetMapping("/management/editClient")
    public String editClient(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model){
        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        model.addAttribute(CLIENT, clientService.getDTO(id));
        return EDIT;
    }

    @PostMapping("/management/editClient")
    public String updateClient(@ModelAttribute(CLIENT) @Valid ClientDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute(CLIENT, dto);
            return EDIT;
        }
        try {
            clientService.update(dto);
        } catch (ServiceException e) {
            model.addAttribute(CLIENT, dto);
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
