package controllers;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.ClientServiceI;
import services.ContractServiceI;
import services.OptionServiceI;
import services.TariffServiceI;

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

    @PostMapping("/management/findContract")
    public String find(@RequestParam("number") int number, Model model){
        model.addAttribute("contract",contractService.findByPhone(number));
        return "management/contract/edit-contract";
    }

    @RequestMapping("/management/contracts")
        public String show(){
        return "management/contract/contract-management";
        }

    @GetMapping("/management/createContract")
    public String createShow(@RequestParam("id") int clientId,Model model){
        Contract contract=contractService.create(new Contract(clientServiceI.get(clientId)));
        model.addAttribute("editedContract",contract);
        return "management/contract/edit-contract";
    }

    @ModelAttribute("allOptions")
    public List<TariffOption> getAllOptions(){
        return optionService.getAll();
    }

    @ModelAttribute("allTariffs")
    public List<Tariff> getAllTariffs(){
        return tariffService.getAll();
    }


}
