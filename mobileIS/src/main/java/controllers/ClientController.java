package controllers;

import model.dto.ClientDTO;
import model.entity.Client;
import model.helpers.PaginateHelper;
import model.helpers.Passport;
import model.helpers.Phone;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.interfaces.ClientServiceI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ClientController {
    private static final String MODEL_MESSAGE = "message";
    private static final String SAVE = "management/client/show-client";
    private static final String CREATE = "management/client/create-client";
    private static final String MANAGEMENT = "management/client/client-management";
    private static final String CLIENT = "client";
    private static final int ROW_PER_PAGE = 3; //specific number of rows per page in the table with all clients

    private static final Logger logger = Logger.getLogger(ClientController.class);

    @Autowired
    ClientServiceI clientService;


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
        if (result.hasErrors()) {
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }

        Optional<String> error = clientService.create(dto);
        if (error.isPresent()) {
            model.addAttribute(MODEL_MESSAGE, error.get());
            model.addAttribute(CLIENT, dto);
            return CREATE;
        }
        model.addAttribute(CLIENT, dto);
        return SAVE;
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

        Optional<String> error = clientService.update(dto);
        if (error.isPresent()) {
            model.addAttribute(CLIENT, dto);
            model.addAttribute(MODEL_MESSAGE, error.get());
            return CREATE;
        }
        attr.addAttribute("id", dto.getId());
        return "redirect:/management/showClient";
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

    @ExceptionHandler({NullPointerException.class})
    public String catchWrongId(HttpServletRequest request, Exception ex) {
        logger.warn("Request: " + request.getRequestURL(), ex);
        return "redirect:/management/clients";
    }
}
