package controllers;

import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.ContractServiceI;

import javax.validation.Valid;

@Controller
public class ContractController {
    @Autowired
    ContractServiceI contractService;

    @PostMapping("/management/findContract")
    public String find(@RequestParam("number") int number, Model model){
        model.addAttribute("contract",contractService.findByPhone(number));
        return "management/contract/edit-contract";
    }

    @RequestMapping("/management/contracts")
        public String show(){
        return "management/contract/contract-management";
        }

        @PostMapping("/management/createContract")
        public String create(@Valid Contract contract, BindingResult result){
        if (result.hasErrors())
            return "/management/contract/create-contract";
            contractService.create(contract);
        return "redirect:/management/contracts";
        }

    @GetMapping("/management/createContract")
    public String createShow(){
        return "management/contract/create-contract";
    }

        @ModelAttribute("contract")
    public Contract formBackingObject(){
        return new Contract();
        }


}
