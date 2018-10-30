package controllers;

import entities.Contract;
import entities.dto.ContractDTO;
import entities.dto.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.*;
import services.implementations.ClientService;
import services.implementations.ContractService;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ContractController {

    private static final String ERROR_ATTRIBUTE = "error";
    private static final String MODEL_MESSAGE = "message";
    private static final String MANAGEMENT="management/contract/contract-management";


    @Autowired
    ContractService contractService;
    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    ClientService clientService;

    @PostMapping("/management/findContract")
    public String find(@Valid Phone phone, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors())
            return MANAGEMENT;

        Contract contract = contractService.findByPhone(Long.parseLong(phone.getPhoneNumber()));
        if (contract == null) {
            model.addAttribute(MODEL_MESSAGE, "no such phone number exists");
            return MANAGEMENT;
        }
        attr.addAttribute("contract", contract);
        return "management/contract/show-contract";
    }

    @RequestMapping("/management/contracts")
    public String show() {
        return MANAGEMENT;
    }

    @GetMapping("/management/showContract")
    public String show(@RequestParam("id") int id, Model model) {
        ContractDTO contract = contractService.getDTO(id);
        model.addAttribute("contract", contract);
        return "management/contract/show-contract";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        ContractDTO dto = new ContractDTO(clientId);
        model.addAttribute("contract", dto);
        return "management/contract/create-contract";
    }

    @PostMapping("management/createContract")
    public String edit(@ModelAttribute("contract") ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            contractService.create(dto);
        } catch (ServiceException e) {
            model.addAttribute("contract", dto);
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            return "management/contract/create-contract";
        }
        attr.addAttribute("id", dto.getId());

        return "redirect:/management/showContract";
    }

    @GetMapping("/management/editContract")
    public String editOption(@RequestParam("id") int id,
                             @RequestParam(value = ERROR_ATTRIBUTE, required = false) String error,
                             Model model) {

        if (error != null) model.addAttribute(MODEL_MESSAGE, error);
        /**
         * TODO make Transfer object
         */
        ContractDTO dto = contractService.getDTO(id);
        model.addAttribute("editedContract", dto);
        List<String> newOptions=optionService.getAllNames();
        newOptions.removeIf(o->dto.getOptionsNames().contains(o));
        model.addAttribute("allOptions",newOptions);
        return "management/contract/edit-contract";
    }

    @PostMapping("/management/editContract")
    public String updateOption(@ModelAttribute("editedContract") ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            contractService.update(dto);
        } catch (ServiceException e) {
            model.addAttribute("editedContract",dto);
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            return "management/contract/edit-contract";
        }
        attr.addAttribute("id",dto.getId());
        return "redirect:/management/showContract";
    }

    @GetMapping("/management/deleteContract")
    public String deleteClient(@RequestParam("id") int id, @RequestParam("clientId") int clientId,RedirectAttributes attr) {
        contractService.delete(id);
        attr.addAttribute("id",clientId);
        return "redirect:/management/showClient";
    }


    @ModelAttribute("allContracts")
    public List<Contract> getAll() {
        return contractService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<String> getAllTariffs() {
        return tariffService.getAllNames();
    }

    @ModelAttribute("allOptions")
    public List<String> getAllOptions() {
        return optionService.getAllNames();
    }

    @ModelAttribute("phone")
    public Phone getPhone() {
        return new Phone();
    }

}
