package controllers;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
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
    ContractServiceI contractService;
    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    ClientServiceI clientServiceI;
    @Autowired
    PhoneNumberService phoneNumberService;

    @PostMapping("/management/findContract")
    public String find(@RequestParam("number") int number, Model model) {
        model.addAttribute("contract", contractService.findByPhone(number));
        return "management/contract/edit-contract";
    }

    @RequestMapping("/management/contracts")
    public String show() {
        return "management/contract/contract-management";
    }

    @GetMapping("/management/editContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        long phone = phoneNumberService.getNext();
        Contract contract = new Contract(phone, clientServiceI.get(clientId));
        model.addAttribute("contract",contract);
        model.addAttribute("clientId",clientId);
        return "management/contract/edit-contract";
    }

    @PostMapping("management/editContract")
    public String edit(@ModelAttribute("contract") @Valid Contract contract, BindingResult result,RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "management/contract/edit-contract";
        }
        contractService.create(contract);
        attributes.addAttribute("clientId",contract.getOwner().getId());
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


}
