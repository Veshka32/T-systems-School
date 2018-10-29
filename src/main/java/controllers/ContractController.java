package controllers;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import entities.dto.ContractDTO;
import entities.dto.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ContractController {
    @Autowired
    ContractService contractService;
    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    ClientService clientService;

    @PostMapping("/management/findContract")
    public String find(@Valid Phone phone, BindingResult result, RedirectAttributes attr, Model model){
        if (result.hasErrors())
            return "management/contract/contract-management";

        Contract contract=contractService.findByPhone(Long.parseLong(phone.getPhoneNumber()));
        if (contract==null){
            model.addAttribute("message","no such phone number exists");
            return "management/contract/contract-management";
        }
        attr.addAttribute("contract",contract);
        return "/management/contract/save-result";
    }

    @RequestMapping("/management/contracts")
    public String show() {
        return "management/contract/contract-management";
    }

    @GetMapping("/management/showContract")
    public String show(@RequestParam("id") int id, Model model) {
        Contract contract = contractService.get(id);
model.addAttribute("contract",contract);
return "management/tariff/save-result";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        ContractDTO dto=new ContractDTO(clientId);
        model.addAttribute("contract",dto);
        return "management/contract/create-contract";
    }

    @PostMapping("management/createContract")
    public String edit(@ModelAttribute("contract") ContractDTO dto, Model model,RedirectAttributes attr) {
        try{
           contractService.create(dto);
        } catch (ServiceException e){
            model.addAttribute("contract",dto);
            model.addAttribute("message",e.getMessage());
            return "management/contract/create-contract";
        }
        attr.addAttribute("id",dto.getId());

        return "redirect:/management/showTariff";
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
    public Phone getPhone(){
        return new Phone();
    }

}
