package controllers;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
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
    @Autowired
    PhoneNumberService phoneNumberService;

    @PostMapping("/management/findContract")
    public String find(@Valid Phone phone, BindingResult result, RedirectAttributes attr){
        if (result.hasErrors())
            return "management/contract/contract-management";
        Contract contract=contractService.findByPhone(Long.parseLong(phone.getPhoneNumber()));
        attr.addAttribute("contract",contract);
        return "/management/contract/edit-contract";
    }

    @RequestMapping("/management/contracts")
    public String show() {
        return "management/contract/contract-management";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        long phone = phoneNumberService.getNext();
        model.addAttribute("clientId",clientId);
        model.addAttribute("phone",phone);
        return "management/contract/create-contract";
    }

    @PostMapping("management/createContract")
    public String edit(@ModelAttribute("contract") @Valid Contract contract,
                       BindingResult result,
                       @RequestParam("clientId") int clientId,
                       @RequestParam("phone") long phone,
                       RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "management/contract/create-contract";
        }
        contract.setNumber(phone);
        clientService.addContract(clientId,contract);
        attr.addAttribute("clientId",clientId);
        return "redirect:/management/editClient";
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions() {
        return optionService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs() {
        return tariffService.getAll();
    }

    @ModelAttribute("contract")
    public Contract getNewContract() {
        return new Contract();
    }

    @ModelAttribute("phone")
    public Phone getPhone(){
        return new Phone();
    }


}
