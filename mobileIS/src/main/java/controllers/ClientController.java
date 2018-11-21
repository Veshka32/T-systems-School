package controllers;

import model.dto.ClientDTO;
import model.entity.Client;
import model.helpers.PaginateHelper;
import model.helpers.Passport;
import model.helpers.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.exceptions.ServiceException;
import services.interfaces.ClientServiceI;
import services.interfaces.ContractServiceI;

import javax.validation.Valid;

@Controller
public class ClientController {
    private static final String MODEL_MESSAGE="message";
    private static final String SAVE="management/client/show-client";
    private static final String CREATE = "management/client/create-client";
    private static final String MANAGEMENT="management/client/client-management";
    private static final String CLIENT = "client";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all clients

    @Autowired
    ClientServiceI clientService;

    @Autowired
    ContractServiceI contractService;

    @RequestMapping("/management/clients")
    public String show(@RequestParam(value = "page", required = false) Integer page, Model model) {
        buildModel(page, model);
        return MANAGEMENT;
    }

    @GetMapping("/management/showClient")
    public String showClient(@RequestParam("id") int id, Model model) {
        ClientDTO client = clientService.getDTO(id);
        model.addAttribute(CLIENT, client);
        return SAVE;
    }

    @GetMapping("management/findClientByPhone")
    @ResponseBody
    public String findByPhone(@RequestParam(value = "phone", required = false) String phone) {
        return clientService.getJsonByPhone(phone);
    }

    @GetMapping("management/findClientByPassport")
    @ResponseBody
    public String findByPassport(@RequestParam(value = "passport", required = false) String phone) {
        return clientService.getJsonByPassport(phone);
    }

    @GetMapping("/management/createClient")
    public String create(Model model) {
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
            model.addAttribute(CLIENT, dto);
            return SAVE;
        } catch (ServiceException e){
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }
    }

    @GetMapping("/management/editClient")
    public String editClient(@RequestParam("id") int id, Model model) {
        model.addAttribute(CLIENT, clientService.getDTO(id));
        return CREATE;
    }

    @PostMapping("/management/editClient")
    public String editClient(@ModelAttribute(CLIENT) @Valid ClientDTO dto, BindingResult result, Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }
        try {
            clientService.update(dto);
            attr.addAttribute("id", dto.getId());
            return "redirect:/management/showClient";
        } catch (ServiceException e) {
            model.addAttribute(CLIENT, dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return CREATE;
        }

    }

    @PostMapping("/management/deleteClient")
    public String deleteClient(@RequestParam("id") int id) {
        clientService.delete(id);
        return "redirect:/management/clients";
    }

    private void buildModel(Integer page, Model model) {
        PaginateHelper<Client> helper = clientService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
        model.addAttribute("phone", new Phone());
        model.addAttribute("passport", new Passport());
    }
}
